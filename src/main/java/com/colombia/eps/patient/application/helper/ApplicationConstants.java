package com.colombia.eps.patient.application.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationConstants {
    public static final String PATIENT_EXIST = "usuario_activo";
    public static final String PATIENT_INACTIVE = "Usuario_inactivo";
    public static final String PATIENT_RETIRED = "usuario_retirado";
}
