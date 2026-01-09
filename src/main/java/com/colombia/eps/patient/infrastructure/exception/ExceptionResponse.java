package com.colombia.eps.patient.infrastructure.exception;

import lombok.Getter;

@Getter
public enum ExceptionResponse {

    IDENTITY_NOT_FOUND("La identidad no existe"),
    CREATE_SES_IDENTITY("Error al crear la identidad en ses"),
    GET_VERIFICATION_STATUS_IN_SES("Error al obtener el estado de verificación de ses"),
    GET_PATIENT("Error al obtener los datos del paciente"),
    SAVE_PHOTO_TO_PATIENT("Error al guardar la foto");
    private final String  message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}
