package com.colombia.eps.patient.application.handler;

import com.colombia.eps.patient.application.dto.PatientRequest;
import com.colombia.eps.patient.application.dto.RequestResponse;
import com.colombia.eps.patient.application.mapper.IPatientMapper;
import com.colombia.eps.patient.domain.api.IPatientServicePort;
import com.colombia.eps.patient.domain.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientHandler implements IPatientHandler{
    private final IPatientMapper patientMapper;
    private final IPatientServicePort patientServicePort;
    /**
     * @param patientRequest to registry
     */
    @Override
    public String createPatient(PatientRequest patientRequest) {
        Patient patient = patientMapper.toPatient(patientRequest);
        return patientServicePort.createPatient(patient);
    }

    /**
     * @param email email´s patient to get
     * @return patient
     */
    @Override
    public RequestResponse getPatient(String email) {
        Patient patient = patientServicePort.getPatient(email);
        return patientMapper.toRequestResponse(patient);
    }

    /**
     * @param email email´s patient to  validate
     * @return String
     */
    @Override
    public String validateEmail(String email) {
        return patientServicePort.validateEmail(email);
    }
}
