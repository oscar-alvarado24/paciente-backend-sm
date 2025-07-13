package com.colombia.eps.patient.infrastructure.exception;

public class DynamoTableNotCreatedException extends RuntimeException {
    public DynamoTableNotCreatedException(String message) {
        super(message);
    }
}
