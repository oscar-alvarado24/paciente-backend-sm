package com.medicine.patient.infrastructure.output.dynamo.adapter;

import com.medicine.patient.domain.model.Patient;
import com.medicine.patient.domain.spi.IPatientPersistencePort;
import com.medicine.patient.infrastructure.exception.ErrorConsultingPatient;
import com.medicine.patient.infrastructure.exception.PatienNotCretedException;
import com.medicine.patient.infrastructure.exception.PatientNotFoundException;
import com.medicine.patient.infrastructure.output.dynamo.config.DynamoDbManager;
import com.medicine.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.medicine.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.medicine.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.medicine.patient.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PatientDynamoAdapter implements IPatientPersistencePort {
    private final IPatientRepository patientRepository;
    private final IPatientEntityMapper patientMapper;

    /**
     * @param patient to save
     */
    @Override
    public String createPatient(Patient patient) {
        PatientEntity patientEntity = patientMapper.toPatientEntity(patient);
        try (DynamoDbManager manager = new DynamoDbManager()) {
            return patientRepository.save(patientEntity, manager.createTable(Constants.TABLE_PATIENT_NAME));
        } catch (Exception exception) {
            throw new PatienNotCretedException(String.format(Constants.MSG_PATIENT_NOT_CREATED, patient.getFirstName(), patient.getFirstSurName()));
        }

    }

    /**
     * @param email patient to get
     * @return patient
     */
    @Override
    public Patient getPatient(String email) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity patientEntity = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND, email)));
            return patientMapper.toPatient(patientEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof PatientNotFoundException) {
                throw e;
            }
            throw new ErrorConsultingPatient(String.format(Constants.ERROR_CONSULTING_PATIENT, email));
        }
    }

    /**
     * @param email email´s patient to  validate
     * @return String
     */
    @Override
    public String validateEmail(String email) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            Optional<PatientEntity> patient = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME));
            return patient.isPresent() ? Constants.PATIENT_EXIST : Constants.PATIENT_DONT_EXIST;
        } catch (Exception e) {
            throw new ErrorConsultingPatient(String.format(Constants.ERROR_CONSULTING_PATIENT, email));
        }

    }
}