package com.medicine.register.infrastructure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String REGION_AWS = "us-east-1";
    public static final String ENDPOINT_DYNAMO = "dynamodb.us-east-1.amazonaws.com";
    public static final String BASE_PACKAGES_REPOSITORY = "com.medicine.register.infrastructure.output.dynamo.repository";
    public static final String MSG_CREATE_PATIENT = "Se crea el paciente %s %s correctamente";
    public static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    public static final String TYPE_KEYSTORE = "JKS";
    public static final String NOT_FOUND = "No encontrada";
    public static final String PATIENT_NOT_FOUND = "El paciente con cedula %s no existe";
}
