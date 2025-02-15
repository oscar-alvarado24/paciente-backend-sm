package com.colombia.eps.patient.domain.spi;

import com.colombia.eps.patient.domain.model.Patient;

public interface IPatientPersistencePort {
    String createPatient (Patient patient);
    Patient getPatient (String email);
    String validatePatient(String email);

    String changeStatus(int id, String status);
}
