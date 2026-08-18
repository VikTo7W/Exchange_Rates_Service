package com.yourcompany.rates.common.exception;

public class UnimplementedResponseTypeException extends DomainException{
    public UnimplementedResponseTypeException(String sourceName) {
        super("Undefined response type from the source: " + sourceName);
    }
}
