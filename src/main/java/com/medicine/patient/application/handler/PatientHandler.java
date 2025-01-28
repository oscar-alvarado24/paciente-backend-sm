package com.medicine.patient.application.handler;

import com.medicine.patient.application.dto.PatientRequest;
import com.medicine.patient.application.dto.RequestResponse;
import com.medicine.patient.application.mapper.IPatientMapper;
import com.medicine.patient.domain.api.IPatientEncryptionServicePort;
import com.medicine.patient.domain.api.IPatientServicePort;
import com.medicine.patient.domain.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientHandler implements IPatientHandler{
    private final IPatientMapper patientMapper;
    private final IPatientServicePort patientServicePort;
    private final IPatientEncryptionServicePort patientEncryptionServicePort;
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
    public String validateStatus(String email) {
        return patientServicePort.validateStatus(email);
    }

    /**
     * @param id of patient to change status
     * @param status to apply
     * @return confirmation message of successfully process
     */
    @Override
    public String changeStatus(int id, String status) {
        return patientServicePort.changeStatus(id, status);
    }

}
