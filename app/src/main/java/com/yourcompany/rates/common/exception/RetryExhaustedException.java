package com.yourcompany.rates.common.exception;

public class RetryExhaustedException extends DomainException {
    public RetryExhaustedException(Throwable cause) {
        super("Retry attempts exhausted", cause);
    }
}
