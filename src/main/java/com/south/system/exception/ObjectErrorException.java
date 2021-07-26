package com.south.system.exception;

public class ObjectErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ObjectErrorException(String message) {
        super(message);
    }
}