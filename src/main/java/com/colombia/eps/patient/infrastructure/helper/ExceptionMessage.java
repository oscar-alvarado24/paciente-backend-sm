package com.colombia.eps.patient.infrastructure.helper;

import lombok.Builder;

@Builder
public class ExceptionMessage {
    private String message;
    private String type;
    private String hour;
    private String line;

    @Override
    public String toString() {
        return String.format("Exception {%n message:%s%n type:%s%n hour:%s%n line: %s)", message, type, hour, line);
    }
}
