package com.colombia.eps.patient.infrastructure.helper;

import lombok.Data;

@Data
public class ErrorInfo {
    private final String exceptionType;
    private final String message;
    private final String className;
    private final String methodName;
    private final String fileName;
    private final int lineNumber;
}