package com.colombia.eps.patient.application.handler;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.PatientResponse;

public interface IPatientHandler {
    String createPatient(PatientRequest patient);

    PatientResponse getPatient (String id);

    String changeStatus(int id, String status);

    String savePhoto(String email, String photo);

    String updatePatient(int id, PatientRequest patient);

    String validateStatusSesRegistration(String email);
}
