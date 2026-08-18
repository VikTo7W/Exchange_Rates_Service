package com.yourcompany.rates.common.exception;

public class DbItemNotFoundException extends DomainException{
    public DbItemNotFoundException (String message) {
        super(message);
    }
}
