package com.medicine.register.application.handler;

import com.medicine.register.application.dto.PatientRequest;
import com.medicine.register.application.dto.RequestResponse;
import com.medicine.register.application.mapper.IPatientMapper;
import com.medicine.register.domain.api.IPatientEncryptionServicePort;
import com.medicine.register.domain.api.IPatientServicePort;
import com.medicine.register.domain.model.Patient;
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
        String passwordEncryption = patientEncryptionServicePort.encrypt(patientRequest.getPassword());
        Patient patient = patientMapper.toPatient(patientRequest, passwordEncryption);
        patientServicePort.createPatient(patient);
    }

    /**
     * @param id patient to get
     * @return patient
     */
    @Override
    public RequestResponse getPatient(String id) {
        Patient patient = patientServicePort.getPatient(id);
        return patientMapper.toRequestResponse(patient);
    }
}
