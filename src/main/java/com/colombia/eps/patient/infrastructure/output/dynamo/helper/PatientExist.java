package com.colombia.eps.patient.infrastructure.output.dynamo.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PatientExist {
    EMAIL (" email %s"),
    ID (" id %s"),
    BOTH(" email %s y id %s"),
    NO_EXIST("No existe un paciente con email %s ni id %");
    private final String message;

    public String format(Object... arg) {
        return String.format(message,  arg);
    }
}
