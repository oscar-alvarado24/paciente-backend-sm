package com.medicine.patient.application.handler;

import com.medicine.patient.application.dto.PatientRequest;
import com.medicine.patient.application.dto.RequestResponse;

public interface IPatientHandler {
    String createPatient(PatientRequest patient);
    RequestResponse getPatient (String id);

    String validateStatus(String email);

    String changeStatus(int id, String status);
}