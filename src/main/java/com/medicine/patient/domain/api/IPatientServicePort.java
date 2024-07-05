package com.medicine.register.domain.api;

import com.medicine.register.domain.model.Patient;

public interface IPatientServicePort {
    void createPatient(Patient patient);

    Patient getPatient(String email);

    String validateEmail(String email);
}
