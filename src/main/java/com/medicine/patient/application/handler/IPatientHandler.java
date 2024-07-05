package com.medicine.patient.application.handler;

import com.medicine.patient.application.dto.PatientRequest;
import com.medicine.patient.application.dto.RequestResponse;

public interface IPatientHandler {
    void createPatient(PatientRequest patient);
    RequestResponse getPatient (String id);

    String validateEmail(String email);
}
