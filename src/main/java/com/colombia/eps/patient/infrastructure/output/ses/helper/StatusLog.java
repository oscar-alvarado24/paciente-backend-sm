package com.colombia.eps.patient.infrastructure.output.ses.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusLog {
    SUCCESS("Email ya está verificado: %s"),
    NOT_SUCCESS("Enviando nueva solicitud de verificación para: %s"),
    NOT_FOUND("No se encontraron atributos de verificación para: %s");
    private final String sentence;

    public String format(String email) {
        return String.format(sentence,  email);
    }

    }
