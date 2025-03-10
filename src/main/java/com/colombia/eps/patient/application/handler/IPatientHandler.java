package com.colombia.eps.patient.application.handler;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.PatientResponse;

public interface IPatientHandler {
    String createPatient(PatientRequest patient);
    PatientResponse getPatient (String id);

    String validateStatus(String email);
    String changeStatus(int id, String status);
}
