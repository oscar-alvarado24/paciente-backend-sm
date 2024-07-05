package com.medicine.patient.application.handler;

import com.medicine.patient.application.dto.PatientRequest;
import com.medicine.patient.application.dto.RequestResponse;
import com.medicine.patient.application.mapper.IPatientMapper;
import com.medicine.patient.domain.api.IPatientEncryptionServicePort;
import com.medicine.patient.domain.api.IPatientServicePort;
import com.medicine.patient.domain.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientHandler implements IPatientHandler{
    private final IPatientMapper patientMapper;
    private final IPatientServicePort patientServicePort;
    private final IPatientEncryptionServicePort patientEncryptionServicePort;
    /**
     * @param patientRequest to registry
     */
    @Override
    public void createPatient(PatientRequest patientRequest) {
        Patient patient = patientMapper.toPatient(patientRequest);
        patientServicePort.createPatient(patient);
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
