package com.colombia.eps.patient.infrastructure.output.sqs.entity;

import jakarta.validation.constraints.NotNull;

public record SqsEntity (
        String patientName,
        String identificationNumber,
        String affiliationDate,
        String planType,
        String email,
        String messageType,
        String channels,
        Boolean sesVerified
) {
    @Override
    @SuppressWarnings("NullableProblems")
    public @NotNull String toString() {
        return "{" +
                "\"message_type\":\"" + messageType + "\"," +
                "\"channels\":\"" + channels + "\"," +
                "\"patient_name\":\"" + patientName + "\"," +
                "\"identification_number\":\"" + identificationNumber + "\"," +
                "\"affiliation_date\":\"" + affiliationDate + "\"," +
                "\"plan_type\":\"" + planType + "\"," +
                "\"patient_email\":\"" + email + "\"," +
                "\"ses_verified\":" + sesVerified +
                "}";
    }
}
