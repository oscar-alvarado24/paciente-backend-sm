package com.medicine.patient.infrastructure.exception;

public class ErrorValidatingPatient extends RuntimeException {
    public ErrorValidatingPatient(String message) {
        super(message);
    }
}
