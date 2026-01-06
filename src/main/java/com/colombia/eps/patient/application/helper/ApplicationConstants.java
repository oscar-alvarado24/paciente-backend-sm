package com.colombia.eps.patient.application.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationConstants {
    public static final String PATIENT_EXIST = "usuario_activo";
    public static final String PATIENT_INACTIVE = "usuario_inactivo";
    public static final String PATIENT_RETIRED = "usuario_retirado";
    public static final String MSG_ERROR_CRYPTO = "Error al ejecutar el proceso de %s del dato";
    public static final String ENCRYPT = "encriptado";
    public static final String DECRYPT = "desencriptado";
    public static final String ALGORITHM = "AES/GCM/NoPadding";
}
