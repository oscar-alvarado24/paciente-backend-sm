package com.medicine.patient.infrastructure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


public interface Constants {

    String REGION_AWS = "us-east-1";
    String ENDPOINT_DYNAMO = "dynamodb.us-east-1.amazonaws.com";
    String BASE_PACKAGES_REPOSITORY = "com.medicine.patient.infrastructure.output.dynamo.repository";
    String MSG_CREATE_PATIENT = "Se crea el paciente %s %s correctamente";
    String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    String TYPE_KEYSTORE = "JKS";
    String PATIENT_NOT_FOUND = "El paciente con email %s no existe";
    String PATIENT_EXIST = "email_registrado";
    String PATIENT_DONT_EXIST = "email_no_registrado";
}

