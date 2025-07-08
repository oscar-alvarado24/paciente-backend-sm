package com.colombia.eps.patient.infrastructure.output.dynamo.adapter;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.ErrorConsultingPatient;
import com.colombia.eps.patient.infrastructure.exception.ErrorUpdatingPatient;
import com.colombia.eps.patient.infrastructure.exception.ErrorValidatingPatient;
import com.colombia.eps.patient.infrastructure.exception.GetPhotoException;
import com.colombia.eps.patient.infrastructure.exception.PatienNotCretedException;
import com.colombia.eps.patient.infrastructure.exception.PatientNotFoundException;
import com.colombia.eps.patient.infrastructure.exception.SavePhotoToPatientException;
import com.colombia.eps.patient.infrastructure.output.dynamo.config.DynamoDbManager;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.Status;
import com.colombia.eps.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.colombia.eps.patient.infrastructure.util.Constants;
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
        patientEntity.setPhoto("");
        try (DynamoDbManager manager = new DynamoDbManager()) {
            return patientRepository.save(patientEntity, manager.createTable(Constants.TABLE_PATIENT_NAME));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            if (exception instanceof PatienNotCretedException) {
                throw exception;
            }
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

    /**
     * @param email of patient to get photo
     * @return photo of patient
     */
    @Override
    public String getPhoto(String email) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity  patient = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND, Constants.EMAIL, email)));
            return patient.getPhoto();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GetPhotoException(String.format(Constants.ERROR_GET_PHOTO, email));
        }

    }

    /**
     * @param email of patient to save photo
     * @param photo to save
     * @return confirmation message
     */
    @Override
    public String savePhoto(String email, String photo) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity patient = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND, Constants.EMAIL, email)));
            patient.setPhoto(photo);
            return patientRepository.updatePatient(patient, manager.createTable(Constants.TABLE_PATIENT_NAME));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SavePhotoToPatientException(String.format(Constants.ERROR_SAVE_PHOTO, email));
        }
    }

    /**
     * @param id of patient to update
     * @param patient to update
     * @return confirmation message
     */
    @Override
    public String updatePatient(int id, Patient patient) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity patientEntity = patientRepository.findPatientById(String.valueOf(id), manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND, Constants.ID, id)));
            PatientEntity patientUpdated = patientMapper.toPatientEntity(patient);
            patientUpdated.setPhoto(patientEntity.getPhoto());
            patientUpdated.setStatus(patientEntity.getStatus());
            return patientRepository.updatePatient(patientUpdated, manager.createTable(Constants.TABLE_PATIENT_NAME));
            }catch (Exception e){
            log.error(e.getMessage());
            throw new ErrorUpdatingPatient(String.format(Constants.ERROR_UPDATING_PATIENT, Constants.PATIENT, Constants.ID.concat(" " + id)));
        }
    }
}