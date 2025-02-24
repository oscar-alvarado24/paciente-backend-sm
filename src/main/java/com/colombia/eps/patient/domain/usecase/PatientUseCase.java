package com.colombia.eps.patient.domain.usecase;

import com.colombia.eps.patient.domain.api.IPatientServicePort;
import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;
import com.colombia.eps.patient.domain.spi.ISesPersistencePort;

public class PatientUseCase implements IPatientServicePort {
    private final IPatientPersistencePort patientPersistencePort;
    private final ICognitoPersistencePort cognitoPersistencePort;
    private final ISesPersistencePort sesPersistencePort;

    public PatientUseCase(IPatientPersistencePort patientPersistencePort, ICognitoPersistencePort cognitoPersistencePort, ISesPersistencePort sesPersistencePort) {
        this.patientPersistencePort = patientPersistencePort;
        this.cognitoPersistencePort = cognitoPersistencePort;
        this.sesPersistencePort = sesPersistencePort;
    }

    /**
     * @param patient to save
     */
    @Override
    public String createPatient(Patient patient) {
        sesPersistencePort.verifyEmailAddress(patient.getEmail());
        cognitoPersistencePort.createPatientInUserPool(patient);
        return patientPersistencePort.createPatient(patient);
    }

    /**
     * @param email´s patient to get
     * @return patient
     */
    @Override
    public Patient getPatient(String email) {
        return patientPersistencePort.getPatient(email);
    }

    /**
     * @param email email´s patient to validate
     * @return string
     */
    @Override
    public String validateStatus(String email) {
        return patientPersistencePort.validatePatient(email);
    }

    /**
     * @param id of  patient to change status
     * @param status to change
     * @return confirmation message
     */
    @Override
    public String changeStatus(int id, String status) {
        return patientPersistencePort.changeStatus(id, status);
    }
}
