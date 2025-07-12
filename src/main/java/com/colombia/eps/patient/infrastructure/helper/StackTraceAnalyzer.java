package com.colombia.eps.patient.infrastructure.helper;

public class StackTraceAnalyzer {

    /**
     * Obtiene información detallada del error desde una excepción
     */
    public static String getErrorInfo(Exception e, String packagePrefix) {
        if (e == null) return null;

        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length == 0) return null;

        // Busca el primer elemento que pertenezca a tu paquete
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(packagePrefix)) {
                return element.toString();
            }
        }
        return stackTrace[0].toString();
    }

}
