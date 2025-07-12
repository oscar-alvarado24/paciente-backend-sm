package com.colombia.eps.patient.domain.spi;

public interface ISesPersistencePort {
    void createSesIdentity(String emailAddress);

    String validateStatusSesRegistration(String email);
}
