package com.yourcompany.rates.common.exception;

public class RetryInterruptedException extends DomainException{
    public RetryInterruptedException(Throwable cause) {
        super("Retry attempts exhausted", cause);
    }
}

