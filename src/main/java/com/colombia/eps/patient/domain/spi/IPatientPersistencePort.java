package com.colombia.eps.patient.domain.spi;

import com.colombia.eps.patient.domain.model.Patient;

public interface IPatientPersistencePort {
    String createPatient (Patient patient);
    Patient getPatient (String email);

    String validateEmail(String email);
}
