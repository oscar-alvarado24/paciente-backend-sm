package com.medicine.patient.infrastructure.exception;

public class ErrorConsultingPatient extends RuntimeException {
    public ErrorConsultingPatient(String message) {
        super(message);
    }
}
