package com.yourcompany.rates.service.worker;

import com.yourcompany.rates.data.aggregate.RateBroadcastingSettingAggregate;
import com.yourcompany.rates.data.enums.*;
import com.yourcompany.rates.service.aggregation.RateBroadcastingSettingAggregateService;
import com.yourcompany.rates.service.crud.CurrencyPairPerSourceService;
import com.yourcompany.rates.service.crud.FilterService;
import com.yourcompany.rates.service.crud.FixRateService;
import com.yourcompany.rates.service.crud.RateSourceService;
import com.yourcompany.rates.common.exception.DbItemComponentNotFoundException;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import com.yourcompany.rates.service.messaging.RabbitEventPublisher;
import com.yourcompany.rates.service.worker.dto.ActiveSettingContext;
import com.yourcompany.rates.service.worker.event.*;
import com.yourcompany.rates.service.worker.util.RateRequestAssembler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import okhttp3.Request;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
public class BroadcastingWorkerManager {

    private final RateBroadcastingSettingAggregateService settingService;
    private final ConcurrentHashMap<Integer, ActiveSettingContext> activeSettingsContext = new ConcurrentHashMap<>();
    private final CurrencyPairPerSourceService curPairPerSourceService;
    private final RateSourceService sourceService;
    private final FilterService filterService;
    private final Logger log;

    private final RequestProcessingWorker requestProcessingWorker;
    private Thread requestProcessingThread;

    public BroadcastingWorkerManager(RateBroadcastingSettingAggregateService settingService, CurrencyPairPerSourceService curPairPerSourceService, RateSourceService sourceService, FilterService filterService, RatesEventPublisher eventPublisher, RabbitEventPublisher rabbitEventPublisher, FixRateService fixRateService) {
        this.settingService = settingService;
        this.curPairPerSourceService = curPairPerSourceService;
        this.sourceService = sourceService;
        this.filterService = filterService;
        this.log = Logger.getLogger(BroadcastingWorkerManager.class.getName());
        this.requestProcessingWorker = new RequestProcessingWorker(activeSettingsContext, eventPublisher, fixRateService, rabbitEventPublisher);
    }

    @PostConstruct
    public void start() {
        List<RateBroadcastingSettingAggregate> activeSettings;
        activeSettings = settingService.getAggregatesByStatus(BroadcastingStatus.ACTIVE);

        activeSettings.stream()
            .filter(Objects::nonNull)
            .forEach(this::addSetting);


        requestProcessingThread = new Thread(
            requestProcessingWorker,
            "request-processing-worker"
        );

        requestProcessingThread.start();
        log.info("Worker started");
    }

    private boolean addSetting(RateBroadcastingSettingAggregate setting) {
        ActiveSettingContext ctx = assembleContext(setting);
        if (ctx == null) return false;

        activeSettingsContext.put(
            setting.setting().id(),
            ctx
        );
        log.info("Setting successfully added for broadcasting: " + setting.setting().id());
        return true;
    }

    @PreDestroy
    public void shutdown() {
        requestProcessingWorker.stop();
        requestProcessingThread.interrupt();
        log.info("Worker shutting down");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSettingDeactivated(SettingDeactivatedEvent event) {
        activeSettingsContext.remove(event.settingId());
        log.info("Setting deactivated: " + event.settingId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSettingChangedOrActivated(SettingChangedOrActivatedEvent event) {
        RateBroadcastingSettingAggregate updatedSetting;
        try {
            updatedSetting = settingService.getAggregate(event.settingId());
        } catch (DbItemNotFoundException | DbItemComponentNotFoundException e) {
            log.severe("Unable to update or activate setting for broadcasting: " + event.settingId() + ", Message: " + e.getMessage());
            return;
        }
        if (addSetting(updatedSetting)) log.info("Setting successfully activated or changed: " + event.settingId());
    }

    @EventListener
    public void onSourceDown(SourceDownEvent event) {
        try {
            sourceService.changeStatus(event.sourceId(), SourceStatus.DOWN);
        } catch (DbItemNotFoundException | DbItemComponentNotFoundException | DataIntegrityViolationException e) {
            log.severe("Unable to change the source status to DOWN: " + event.sourceId() + ", Message: " + e.getMessage());
        }
        activeSettingsContext.values().removeIf(ctx -> ctx.setting().source().id() == event.sourceId());
        log.warning("Rate source status changed to DOWN, cause: " + event.reason());
    }

    private ActiveSettingContext assembleContext(RateBroadcastingSettingAggregate setting) {
        Request request = assembleRequest(setting);
        if (request == null) return null;
        return new ActiveSettingContext(setting, request, Duration.ofMillis(setting.setting().broadcastingTimeout()), 3);
    }

    private Request assembleRequest(RateBroadcastingSettingAggregate setting) {
        Request request;
        try {
            RateRequestAssembler assembler = new RateRequestAssembler(filterService, curPairPerSourceService, setting);
            request = assembler.assemble();
        } catch (Exception e) {
            log.severe("Error assembling request for setting with id: " + setting.setting().id() + ", Caused by: " + e.getMessage());
            return null;
        }
        return request;
    }

}

