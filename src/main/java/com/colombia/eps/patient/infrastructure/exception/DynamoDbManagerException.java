package com.colombia.eps.patient.infrastructure.exception;

public class DynamoDbManagerException extends RuntimeException {
    public DynamoDbManagerException(String message) {
        super(message);
    }
}
