package com.yourcompany.rates.service.worker.dto;

import com.yourcompany.rates.data.aggregate.RateBroadcastingSettingAggregate;
import okhttp3.Request;

import java.time.Duration;
import java.time.Instant;

public final class ActiveSettingContext {

    private final RateBroadcastingSettingAggregate setting;
    private final Request request;

    private final Duration baseInterval;
    private final int maxAttempts;

    private volatile Instant nextExecutionTime;
    private int attemptCount = 0;

    public ActiveSettingContext(
        RateBroadcastingSettingAggregate setting,
        Request request,
        Duration baseInterval,
        int maxAttempts
    ) {
        this.setting = setting;
        this.request = request;
        this.baseInterval = baseInterval;
        this.maxAttempts = maxAttempts;
        this.nextExecutionTime = Instant.now();
    }

    public boolean isDue(Instant now) {
        return !now.isBefore(nextExecutionTime);
    }

    public void onDispatch() {
        nextExecutionTime = Instant.now().plus(baseInterval);
    }

    public void onSuccess() {
        attemptCount = 0;
    }

    public boolean onFailureAndCheckExhausted() {
        attemptCount++;

        Duration backoff = baseInterval.multipliedBy(attemptCount * 10L);
        nextExecutionTime = Instant.now().plus(backoff);

        return attemptCount >= maxAttempts;
    }

    public RateBroadcastingSettingAggregate setting() { return setting; }
    public Request request() { return request; }
}
