package com.medicine.patient.infrastructure.output.dynamo.adapter;

import com.medicine.patient.domain.model.Patient;
import com.medicine.patient.domain.spi.IPatientPersistencePort;
import com.medicine.patient.infrastructure.exception.PatientNotFoundException;
import com.medicine.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.medicine.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.medicine.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.medicine.patient.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

    /**
     * @param email email´s patient to  validate
     * @return String
     */
    @Override
    public String validateEmail(String email) {
        Optional<PatientEntity> patient = patientRepository.findByEmail(email);
        return patient.isPresent() ? Constants.PATIENT_EXIST : Constants.PATIENT_DONT_EXIST;
    }
}