package com.yourcompany.rates.common.exception;

public class PostRequestAssemblyException extends DomainException{
    public PostRequestAssemblyException(Throwable cause) {
        super("Error with POST request assembly:", cause);
    }
}
