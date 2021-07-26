package com.south.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new StandardError(notFoundException.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> badRequestException(BadRequestException badRequestException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new StandardError(badRequestException.getMessage()));
    }

    @ExceptionHandler(ObjectErrorException.class)
    public ResponseEntity<StandardError> internalErrorException(ObjectErrorException objectErrorException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new StandardError(objectErrorException.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> internalErrorException(ConflictException conflictException) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(new StandardError(conflictException.getMessage()));
    }
}