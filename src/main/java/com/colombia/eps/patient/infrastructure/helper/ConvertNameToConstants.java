package com.colombia.eps.patient.infrastructure.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConvertNameToConstants {
    public static String exceptionToConstant(String exceptionName) {

        return exceptionName
                .replaceAll("Exception$", "")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase();
    }
}
