package com.medicine.patient.domain.api;

import com.medicine.patient.domain.model.Patient;

public interface IPatientServicePort {
    String createPatient(Patient patient);

    Patient getPatient(String email);

    String validateStatus(String email);

    String changeStatus(int id, String status);
}