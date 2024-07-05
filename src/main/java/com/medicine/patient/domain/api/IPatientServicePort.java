package com.medicine.patient.domain.api;

import com.medicine.patient.domain.model.Patient;

public interface IPatientServicePort {
    void createPatient(Patient patient);

    Patient getPatient(String email);

    String validateEmail(String email);
}
