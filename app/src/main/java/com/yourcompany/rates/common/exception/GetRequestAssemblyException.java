package com.yourcompany.rates.common.exception;

public class GetRequestAssemblyException extends DomainException{
    public GetRequestAssemblyException(Throwable cause) {
        super("Error with GET request assembly:", cause);
    }
}
