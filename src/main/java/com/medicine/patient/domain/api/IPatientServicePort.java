package com.medicine.patient.domain.api;

import com.medicine.patient.domain.model.Patient;

public interface IPatientServicePort {
    String createPatient(Patient patient);

    Patient getPatient(String email);

    String validateEmail(String email);
}
