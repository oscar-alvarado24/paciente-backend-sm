package com.medicine.register.application.handler;

import com.medicine.register.application.dto.PatientRequest;
import com.medicine.register.application.dto.RequestResponse;

public interface IPatientHandler {
    void createPatient(PatientRequest patient);
    RequestResponse getPatient (String id);
}
