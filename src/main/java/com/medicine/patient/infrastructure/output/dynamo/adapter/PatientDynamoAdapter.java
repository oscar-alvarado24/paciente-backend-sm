package com.medicine.register.infrastructure.output.dynamo.adapter;

import com.medicine.register.domain.model.Patient;
import com.medicine.register.domain.spi.IPatientPersistencePort;
import com.medicine.register.infrastructure.exception.PatientNotFoundException;
import com.medicine.register.infrastructure.output.dynamo.entity.PatientEntity;
import com.medicine.register.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.medicine.register.infrastructure.output.dynamo.repository.IPatientRepository;
import com.medicine.register.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PatientDynamoAdapter implements IPatientPersistencePort {
    private final IPatientRepository patientRepository;
    private final IPatientEntityMapper patientMapper;

    /**
     * @param patient to save
     */
    @Override
    public void createPatient(Patient patient) {
        PatientEntity patientEntity = patientMapper.toPatientEntity(patient);
        patientRepository.save(patientEntity);
    }

    /**
     * @param email patient to get
     * @return patient
     */
    @Override
    public Patient getPatient(String email) {
        String exceptionMessage = String.format(Constants.PATIENT_NOT_FOUND, email);
        PatientEntity patientEntity = patientRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException(exceptionMessage));
        return patientMapper.toPatient(patientEntity);
    }
}