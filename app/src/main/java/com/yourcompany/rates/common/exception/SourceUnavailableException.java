package com.yourcompany.rates.common.exception;

public class SourceUnavailableException extends DomainException {
    public SourceUnavailableException(String sourceName) {
        super("Source is DOWN: " + sourceName);
    }
}
