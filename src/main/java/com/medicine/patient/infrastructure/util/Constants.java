package com.medicine.patient.infrastructure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String BASE_PACKAGES_REPOSITORY = "com.medicine.patient.infrastructure.output.dynamo.repository";
    public static final String MSG_CREATE_PATIENT = "Se crea el paciente %s %s correctamente";
    public static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    public static final String TYPE_KEYSTORE = "JKS";
    public static final String PATIENT_NOT_FOUND = "El paciente con email %s no existe";
    public static final String PATIENT_EXIST = "email_registrado";
    public static final String PATIENT_DONT_EXIST = "email_no_registrado";
    public static final String DYNAMO_ROL = "DYNAMO_ROL";
    public static final String ROLE_SESSION_NAME_DYNAMO =  "dynamo-conn";
    public static final String ERROR_CONSULTING_PATIENT = "Fallo interno con el paciente %s.";
    public static final String TABLE_PATIENT_NAME = "pacientes";
    public static final String MSG_PATIENT_NOT_CREATED = "Error al crear al paciente %s %s";
}

