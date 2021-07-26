package com.south.system.exception;

public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConflictException(String message) {
        super(message);
    }
}