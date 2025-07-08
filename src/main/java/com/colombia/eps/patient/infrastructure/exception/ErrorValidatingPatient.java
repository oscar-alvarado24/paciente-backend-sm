package com.colombia.eps.patient.infrastructure.exception;

public class ErrorValidatingPatient extends RuntimeException {
    public ErrorValidatingPatient(String message) {
        super(message);
    }
}
