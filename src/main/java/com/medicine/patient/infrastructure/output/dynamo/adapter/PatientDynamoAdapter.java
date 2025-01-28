package com.medicine.patient.infrastructure.output.dynamo.adapter;

import com.medicine.patient.domain.model.Patient;
import com.medicine.patient.domain.spi.IPatientPersistencePort;
import com.medicine.patient.infrastructure.exception.ErrorConsultingPatient;
import com.medicine.patient.infrastructure.exception.ErrorUpdatingPatient;
import com.medicine.patient.infrastructure.exception.ErrorValidatingPatient;
import com.medicine.patient.infrastructure.exception.PatienNotCretedException;
import com.medicine.patient.infrastructure.exception.PatientNotFoundException;
import com.medicine.patient.infrastructure.output.dynamo.config.DynamoDbManager;
import com.medicine.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.medicine.patient.infrastructure.output.dynamo.entity.Status;
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
        patientEntity.setStatus(Status.ACTIVE);
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
            PatientEntity patientEntity = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND,Constants.EMAIL, email)));
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
    public String validatePatient(String email) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            Optional<PatientEntity> patient = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME));
            String response = "";
            if (patient.isPresent()) {
                switch (patient.get().getStatus()) {
                    case ACTIVE -> response = Constants.PATIENT_EXIST;
                    case INACTIVE -> response = Constants.PATIENT_INACTIVE;
                    case RETIRED -> response = Constants.PATIENT_RETIRED;
                }
            }else {
                response = Constants.PATIENT_DONT_EXIST;
            }
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ErrorValidatingPatient(String.format(Constants.MSG_PROCESS_ERROR, Constants.PROC_VALIDATE_PATIENT,email));
        }
    }

    /**
     * @param id of patient to change status
     * @param status to update
     * @return confirmation message
     */
    @Override
    public String changeStatus(int id, String status) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity  patient = patientRepository.findPatientById(String.valueOf(id), manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND, Constants.ID, id)));
            patient.setStatus(Status.valueOf(status));
            return patientRepository.updatePatient(patient, manager.createTable(Constants.TABLE_PATIENT_NAME));
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ErrorUpdatingPatient(String.format(Constants.ERROR_UPDATING_PATIENT, Constants.UPDATE_STATUS, Constants.ID.concat(" " + id)));
        }
    }
}