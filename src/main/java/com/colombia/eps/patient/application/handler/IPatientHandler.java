package com.colombia.eps.patient.application.handler;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.RequestResponse;

public interface IPatientHandler {
    String createPatient(PatientRequest patient);
    RequestResponse getPatient (String id);

    String validateEmail(String email);
}
