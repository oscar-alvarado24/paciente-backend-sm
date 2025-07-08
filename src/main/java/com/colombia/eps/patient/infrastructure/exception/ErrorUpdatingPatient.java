package com.colombia.eps.patient.infrastructure.exception;

public class ErrorUpdatingPatient extends RuntimeException {
    public ErrorUpdatingPatient(String message) {
        super(message);
    }
}
