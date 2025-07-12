package com.colombia.eps.patient.infrastructure.output.ses.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SesStatus {
    SUCCESS ("Email_verificado"),
    NOT_SUCCESS("Validacion_reenviada");
    private final String sentence;
}
