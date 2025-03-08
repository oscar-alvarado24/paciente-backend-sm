package com.medicine.patient.infrastructure.exception;

public class ErrorUpdatingPatient extends RuntimeException {
    public ErrorUpdatingPatient(String message) {
        super(message);
    }
}
