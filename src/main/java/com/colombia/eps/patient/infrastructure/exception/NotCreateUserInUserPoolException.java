package com.colombia.eps.patient.infrastructure.exception;

public class NotCreateUserInUserPoolException extends RuntimeException {
    public NotCreateUserInUserPoolException(String message) {
        super(message);
    }

    public static class VerifyEmailAddressException extends RuntimeException {
        public VerifyEmailAddressException(String message) {
            super(message);
        }
    }
}
