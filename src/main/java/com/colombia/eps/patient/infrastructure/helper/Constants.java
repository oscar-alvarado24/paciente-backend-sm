package com.colombia.eps.patient.infrastructure.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String VE_REGION = "REGION";
    public static final String VE_COGNITO_ROL = "COGNITO_ROL";
    public static final String VE_USER_POOL_ID = "USER_POOL_ID";
    public static final String VE_AKI_DYNAMO_USER = "AKI_DYNAMO_USER";
    public static final String VE_SAK_DYNAMO_USER = "SAK_DYNAMO_USER";
    public static final String VE_SAK_COGNITO_USER = "SAK_COGNITO_USER";
    public static final String VE_AKI_COGNITO_USER = "AKI_COGNITO_USER";
    public static final String MSG_UPDATE_PATIENT = "Paciente actualizado de forma correcta";
    public static final String MSG_CREATE_PATIENT = "Se crea el paciente %s %s correctamente";
    public static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    public static final String TYPE_KEYSTORE = "JKS";
    public static final String PATIENT_NOT_FOUND = "El paciente con %s %s no existe";
    public static final String DYNAMO_ROL = "DYNAMO_ROL";
    public static final String ROLE_SESSION_NAME_DYNAMO =  "dynamo-patient";
    public static final String ERROR_CONSULTING_PATIENT = "Fallo interno con el paciente %s.";
    public static final String TABLE_PATIENT_NAME = "patient";
    public static final String MSG_PATIENT_NOT_CREATED = "Error al crear al paciente %s %s";
    public static final String ROLE_SESSION_NAME_COGNITO = "patient_service";
    public static final String MSG_NOT_CREATE_USER_IN_USER_POOL = "No se pudo crear en cognito el paciente  %s";
    public static final String MSG_NOT_ADD_PATIENT_TO_PATIENT_GROUP = "No se pudo agregar al paciente %s %s al grupo de pacientes de cognito";
    public static final String PATIENT_GROUP = "PATIENT_GROUP";
    public static final String ASTERISK = "*";
    public static final String MSG_FAIL_PROCESS_FOR_CREATE_USER = "Falló el proceso de cacion del usuario en cognito";
    public static final String MSG_PROCESS_ERROR = "Fallo en el proceso %s con el email %s";
    public static final String PROC_VALIDATE_PATIENT = "validar paciente ";
    public static final String EMAIL_INDEX = "email-index";
    public static final String ID = "cedula";
    public static final String EMAIL = "email";
    public static final String ERROR_UPDATING_PATIENT = "Error al actualizar el %s con %s";
    public static final Object UPDATE_STATUS = "estado del paciente ";
    public static final String ROLE_SESSION_NAME_SES = "ses_patient";
    public static final String VE_SES_ROL = "SES_ROL";
    public static final String VE_AKI_SES_USER = "AKI_SES_USER";
    public static final String VE_SAK_SES_USER = "SAK_SES_USER";
    public static final String ERROR_GET_PHOTO = "Error al obtener la foto del paciente con email %s";
    public static final String ERROR_SAVE_PHOTO = "Error al guardar la foto del paciente con email %s";
    public static final String PATIENT = "paciente";
    public static final String SUCCESS = "Success";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String EMAIL_VERIFIED = "email_verified";
    public static final String TRUE = "true";
    public static final String EMPTY = "";
    public static final String MSG_PATIENT_ALREADY_EXISTS = "Existe paciente con %s ";
}

