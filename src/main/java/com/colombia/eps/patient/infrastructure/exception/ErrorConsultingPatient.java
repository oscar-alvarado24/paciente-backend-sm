package com.colombia.eps.patient.infrastructure.exception;

public class ErrorConsultingPatient extends RuntimeException {
    public ErrorConsultingPatient(String message) {
        super(message);
    }
}
