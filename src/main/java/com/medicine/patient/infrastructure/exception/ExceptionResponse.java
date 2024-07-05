package com.medicine.patient.infrastructure.exception;

public enum ExceptionResponse {

    PATIENT_NOT_FOUND("El paciente no existe");
    private final String  message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
