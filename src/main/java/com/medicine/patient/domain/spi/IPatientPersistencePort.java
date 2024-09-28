package com.medicine.patient.domain.spi;

import com.medicine.patient.domain.model.Patient;

public interface IPatientPersistencePort {
    String createPatient (Patient patient);
    Patient getPatient (String email);

    String validateEmail(String email);
}
