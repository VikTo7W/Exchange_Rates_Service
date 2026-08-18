package com.yourcompany.rates.service.worker;

import com.yourcompany.rates.data.enums.RoundingType;
import com.yourcompany.rates.data.enums.StatisticalOperation;
import com.yourcompany.rates.data.model.FixRate;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import com.yourcompany.rates.service.crud.FixRateService;
import com.yourcompany.rates.service.messaging.RabbitEventPublisher;
import com.yourcompany.rates.service.messaging.RateUpdate;
import com.yourcompany.rates.service.worker.dto.ActiveSettingContext;
import com.yourcompany.rates.service.worker.dto.CompletedResponse;
import com.yourcompany.rates.service.worker.event.RatesEventPublisher;
import com.yourcompany.rates.service.worker.event.SourceDownEvent;
import com.yourcompany.rates.service.worker.util.RateResponseProcessor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RequestProcessingWorker implements Runnable {

    private final Map<Integer, ActiveSettingContext> activeSettings;
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private final RatesEventPublisher eventPublisher;
    private final Logger log;
    private final FixRateService fixRateService;
    private final RabbitEventPublisher publisher;
    private volatile boolean running = true;

    public RequestProcessingWorker(
        Map<Integer, ActiveSettingContext> activeSettings,
        RatesEventPublisher eventPublisher,
        FixRateService fixRateService,
        RabbitEventPublisher publisher
    ) {
        this.activeSettings = activeSettings;
        this.eventPublisher = eventPublisher;
        this.fixRateService = fixRateService;
        this.publisher = publisher;
        this.log = Logger.getLogger(RequestProcessingWorker.class.getName());
    }

    @Override
    public void run() {
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                Instant now = Instant.now();

                for (ActiveSettingContext ctx : activeSettings.values()) {
                    if (ctx.isDue(now)) {
                        dispatch(ctx);
                        ctx.onDispatch();
                    }
                }

                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dispatch(ActiveSettingContext ctx) {
        httpClient.newCall(ctx.request()).enqueue(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response){
                process(new CompletedResponse(ctx, response));
            }

            public void onFailure(Call call, IOException e) {
                log.severe("Error sending request for setting with id: " + ctx.setting().setting().id() + " Caused by: " + e.getMessage());
                boolean exhausted = ctx.onFailureAndCheckExhausted();
                if (exhausted) {
                    eventPublisher.publish(new SourceDownEvent(ctx.setting().source().id(), ctx.setting().setting().id(), "Request sending retries exhausted"));
                }
            }
        });
    }

    private void process(CompletedResponse completed) {
        ActiveSettingContext ctx = completed.context();
        BigDecimal rate;
        try {
            List<Double> sourceRates = new RateResponseProcessor(completed).process();
            rate = applyCalculationRule(sourceRates, ctx.setting().calculationRule(), ctx.setting().currencyPair().toCurrency().rate());
        } catch (Exception e) {
            log.severe("Error processing response for setting with id: " + completed.context().setting().setting().id() + " Caused by: " + e.getMessage());
            boolean exhausted = ctx.onFailureAndCheckExhausted();
            if (exhausted) {
                eventPublisher.publish(new SourceDownEvent(ctx.setting().source().id(), ctx.setting().setting().id(), "Response processing retries exhausted"));
            }
            return;
        }

        ctx.onSuccess();
        fixRateService.add(new FixRate(0, ctx.setting().setting().id(), rate, OffsetDateTime.now()));
        publisher.publishRateUpdate(new RateUpdate(ctx.setting().currencyPair().fromCurrency().ticker() + "/" + ctx.setting().currencyPair().toCurrency().ticker(), rate, OffsetDateTime.now(), ctx.setting().setting().id()));
    }

    public void stop() {
        running = false;
    }

    private BigDecimal applyCalculationRule(List<Double> inputRates, SettingCalculationRule calcRule, double multiplicationRate) {

        BigDecimal finalRate;

        if (calcRule.sourceStatOperation() == StatisticalOperation.MEAN) {
            double rateSum = 0;
            for (Double rate : inputRates) rateSum += rate;
            finalRate = new BigDecimal(Double.toString(rateSum / inputRates.size()));
        } else if (calcRule.sourceStatOperation() == StatisticalOperation.MEDIAN) {
            finalRate = inputRates.size()%2 != 0
                ? new BigDecimal(Double.toString(inputRates.get(inputRates.size()/2)))
                : new BigDecimal(Double.toString((inputRates.get(inputRates.size() / 2) + inputRates.get(inputRates.size() / 2 - 1)) / 2));
        } else {
            finalRate = new BigDecimal(Double.toString(inputRates.get(0)));
        }

        finalRate = finalRate.multiply(calcRule.marginPercentage().add(BigDecimal.ONE));

        if (calcRule.roundingType() == RoundingType.NEAREST) {
            return finalRate.setScale((int) Math.log10(multiplicationRate), RoundingMode.HALF_UP);
        } else if (calcRule.roundingType() == RoundingType.CEIL) {
            return finalRate.setScale((int) Math.log10(multiplicationRate), RoundingMode.CEILING);
        } else {
            return finalRate.setScale((int) Math.log10(multiplicationRate), RoundingMode.FLOOR);
        }
    }
}

