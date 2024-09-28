package com.medicine.patient.infrastructure.exception;

public class DynamoDbManagerException extends RuntimeException {
    public DynamoDbManagerException(String message) {
        super(message);
    }
}
