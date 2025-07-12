package com.colombia.eps.patient.infrastructure.output.dynamo.adapter;

import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.infrastructure.exception.ChangePatientStatusException;
import com.colombia.eps.patient.infrastructure.exception.GetPatientException;
import com.colombia.eps.patient.infrastructure.exception.UpdatePatientException;
import com.colombia.eps.patient.infrastructure.exception.PatientAlreadyExistException;
import com.colombia.eps.patient.infrastructure.exception.PatienNotCretedException;
import com.colombia.eps.patient.infrastructure.exception.PatientNotFoundException;
import com.colombia.eps.patient.infrastructure.exception.SavePhotoToPatientException;
import com.colombia.eps.patient.infrastructure.helper.ExceptionMessage;
import com.colombia.eps.patient.infrastructure.helper.StackTraceAnalyzer;
import com.colombia.eps.patient.infrastructure.output.dynamo.config.DynamoDbManager;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.PatientEntity;
import com.colombia.eps.patient.infrastructure.output.dynamo.entity.Status;
import com.colombia.eps.patient.infrastructure.output.dynamo.helper.PatientExist;
import com.colombia.eps.patient.infrastructure.output.dynamo.mapper.IPatientEntityMapper;
import com.colombia.eps.patient.infrastructure.output.dynamo.repository.IPatientRepository;
import com.colombia.eps.patient.infrastructure.helper.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity patientEntity = patientMapper.toPatientEntity(patient);
            patientEntity.setStatus(Status.ACTIVE);
            patientEntity.setPhoto("");
            String response = verifyExistIdEmail(patient.getEmail(), patientEntity.getId(), manager);
            if (!PatientExist.NO_EXIST.format(patient.getEmail(),patientEntity.getId()).equals(response)) {
                throw new PatientAlreadyExistException(String.format(Constants.MSG_PATIENT_ALREADY_EXISTS, response));
            }
            return patientRepository.save(patientEntity, manager.createTable(Constants.TABLE_PATIENT_NAME));
        } catch (PatientAlreadyExistException exception){
            throw exception;
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, PatientDynamoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new PatienNotCretedException(String.format(Constants.MSG_PATIENT_NOT_CREATED, patient.getFirstName(), patient.getFirstSurName()));
        }
    }

    /**
     * search an email and id for verify if exist in a database
     * @param email of patient to verify
     * @param id    of patient to verify
     * @return confirmation message
     */
    private String verifyExistIdEmail(String email, String id, DynamoDbManager manager) {
        // thread 1: Consult by email
        CompletableFuture<Optional<PatientEntity>> emailSearch =
                CompletableFuture.supplyAsync(() -> patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME)));

        // thread 2: Consult by ID
        CompletableFuture<Optional<PatientEntity>> idSearch =
                CompletableFuture.supplyAsync(() -> patientRepository.findPatientById(id, manager.createTable(Constants.TABLE_PATIENT_NAME)));
        String response;
        if (emailSearch.join().isPresent() && idSearch.join().isEmpty()) {
            response = PatientExist.EMAIL.format(email);
        } else if (emailSearch.join().isEmpty() && idSearch.join().isPresent()) {
            response = PatientExist.ID.format(id);
        } else if (emailSearch.join().isPresent() && idSearch.join().isPresent()) {
            response = PatientExist.BOTH.format(email, id);
        } else {
            response = PatientExist.NO_EXIST.format(email, id);
        }
        return response;
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
        } catch (PatientNotFoundException exception){
            throw exception;
        } catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, PatientDynamoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new GetPatientException(String.format(Constants.ERROR_CONSULTING_PATIENT, email));
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
        }catch (Exception exception){
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, PatientDynamoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new ChangePatientStatusException(String.format(Constants.ERROR_UPDATING_PATIENT, Constants.UPDATE_STATUS, Constants.ID.concat(" " + id)));
        }
    }

    /**
     * @param email of patient to save a photo
     * @param photo to save
     * @return confirmation message
     */
    @Override
    public String savePhoto(String email, String photo) {
        try (DynamoDbManager manager = new DynamoDbManager()) {
            PatientEntity patient = patientRepository.findPatientByEmail(email, manager.createTable(Constants.TABLE_PATIENT_NAME)).orElseThrow(() -> new PatientNotFoundException(String.format(Constants.PATIENT_NOT_FOUND, Constants.EMAIL, email)));
            patient.setPhoto(photo);
            return patientRepository.updatePatient(patient, manager.createTable(Constants.TABLE_PATIENT_NAME));
        } catch (PatientNotFoundException e) {
            throw e;
        }catch (Exception exception) {
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, PatientDynamoAdapter.class.getPackageName()))
                    .build()
                    .toString());
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
            patientUpdated.setPhoto(patient.getPhoto() != null || Constants.EMPTY.equals(patient.getPhoto())  ? patient.getPhoto() : patientEntity.getPhoto() );
            patientUpdated.setStatus(patientEntity.getStatus());
            return patientRepository.updatePatient(patientUpdated, manager.createTable(Constants.TABLE_PATIENT_NAME));
        } catch (PatientNotFoundException exception){
            throw exception;
        } catch (Exception exception){
            log.error(ExceptionMessage.builder()
                    .message(exception.getMessage())
                    .type(exception.getClass().getName())
                    .hour(LocalDateTime.now().toString())
                    .line(StackTraceAnalyzer.getErrorInfo(exception, PatientDynamoAdapter.class.getPackageName()))
                    .build()
                    .toString());
            throw new UpdatePatientException(String.format(Constants.ERROR_UPDATING_PATIENT, Constants.PATIENT, Constants.ID.concat(" " + id)));
        }
    }
}