package com.yourcompany.rates.common.exception;

public class InvalidSourcePayloadTypeException extends DomainException{
    public InvalidSourcePayloadTypeException (String message) {
        super(message);
    }
}
