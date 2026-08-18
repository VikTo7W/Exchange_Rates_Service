package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.enums.PayloadType;
import com.yourcompany.rates.data.enums.RequestType;
import com.yourcompany.rates.data.enums.SourceStatus;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.repository.RateBroadcastingSettingRepository;
import com.yourcompany.rates.data.repository.RateSourceRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import com.yourcompany.rates.common.exception.InvalidSourcePayloadTypeException;
import com.yourcompany.rates.service.worker.event.RatesEventPublisher;
import com.yourcompany.rates.service.worker.event.SettingChangedOrActivatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RateSourceService {

    private final RateSourceRepository repository;
    private final RateBroadcastingSettingRepository broadcastingSettingRepository;
    private final RatesEventPublisher eventPublisher;

    public RateSourceService(RateSourceRepository repository, RateBroadcastingSettingRepository broadcastingSettingRepository, RatesEventPublisher eventPublisher) {
        this.repository = repository;
        this.broadcastingSettingRepository = broadcastingSettingRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public RateSource add(RateSource source) {
        if ((source.rateEndpointType() == RequestType.GET && source.rateEndpointPayloadType() != PayloadType.NONE && source.rateEndpointPayloadType() != PayloadType.URL_PARAMETERS) || (source.rateEndpointType() == RequestType.POST && source.rateEndpointPayloadType() != PayloadType.FORM_DATA && source.rateEndpointPayloadType() != PayloadType.JSON)) {
            throw new InvalidSourcePayloadTypeException("Invalid payload type for the specified endpoint type");
        }
        return repository.insert(source);
    }

    @Transactional
    public RateSource getById(int sourceId) {
        return repository.findById(sourceId).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
    }

    @Transactional
    public List<RateSource> getByStatus(SourceStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional
    public List<RateSource> getAll() {
        return repository.findAll();
    }

    @Transactional
    public RateSource changeStatus(int sourceId, SourceStatus status) {
        RateSource updated = repository.updateStatus(sourceId, status).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayload(int sourceId, String payload) {
        RateSource updated = repository.updatePayload(sourceId, payload).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayloadCurrencyFormat(int sourceId, String payloadCurrencyFormat) {
        RateSource updated = repository.updatePayloadCurrencyFormat(sourceId, payloadCurrencyFormat).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayloadFromCurrencyPath(int sourceId, String payloadFromCurrencyPath) {
        RateSource updated = repository.updatePayloadFromCurrencyPath(sourceId, payloadFromCurrencyPath).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayloadToCurrencyPath(int sourceId, String payloadToCurrencyPath) {
        RateSource updated = repository.updatePayloadToCurrencyPath(sourceId, payloadToCurrencyPath).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayloadOrdersTypeKeyPath(int sourceId, String ordersTypeKeyPath) {
        RateSource updated = repository.updatePayloadOrdersTypeKeyPath(sourceId, ordersTypeKeyPath).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayloadBuyOrdersValue(int sourceId, String buyOrdersValue) {
        RateSource updated = repository.updatePayloadBuyOrdersValue(sourceId, buyOrdersValue).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changePayloadSellOrdersValue(int sourceId, String sellOrdersValue) {
        RateSource updated = repository.updatePayloadSellOrdersValue(sourceId, sellOrdersValue).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changeHeaders(int sourceId, String headers) {
        RateSource updated = repository.updateHeaders(sourceId, headers).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changeRateKeyPath(int sourceId, String key) {
        RateSource updated = repository.updateRateKeyPath(sourceId, key).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changeBuyOrdersArrayKeyPath(int sourceId, String key) {
        RateSource updated = repository.updateBuyOrdersArrayKeyPath(sourceId, key).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource changeSellOrdersArrayKeyPath(int sourceId, String key) {
        RateSource updated = repository.updateSellOrdersArrayKeyPath(sourceId, key).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        notifyWorker(sourceId);
        return updated;
    }

    @Transactional
    public RateSource delete(int sourceId) {
        return repository.deleteById(sourceId).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
    }

    private void notifyWorker(int sourceId) {
        List<RateBroadcastingSetting> activeSettings = broadcastingSettingRepository.findByStatus(BroadcastingStatus.ACTIVE);
        activeSettings.stream().filter(setting -> setting.sourceId() == sourceId).forEach(setting -> eventPublisher.publish(new SettingChangedOrActivatedEvent(setting.id())));
    }
}
