package com.colombia.eps.patient.infrastructure.exception;

public class SendQueueFailedException extends RuntimeException {
    public SendQueueFailedException(String message) {
        super(message);
    }
}
