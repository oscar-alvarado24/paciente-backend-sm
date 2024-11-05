package com.colombia.eps.patient.domain.usecase;

import com.colombia.eps.patient.domain.api.IPatientServicePort;
import com.colombia.eps.patient.domain.model.Patient;
import com.colombia.eps.patient.domain.spi.ICognitoPersistencePort;
import com.colombia.eps.patient.domain.spi.IPatientPersistencePort;

public class PatientUseCase implements IPatientServicePort {
    private final IPatientPersistencePort patientPersistencePort;
    private final ICognitoPersistencePort cognitoPersistencePort;

    public PatientUseCase(IPatientPersistencePort patientPersistencePort, ICognitoPersistencePort cognitoPersistencePort) {
        this.patientPersistencePort = patientPersistencePort;
        this.cognitoPersistencePort = cognitoPersistencePort;
    }

    /**
     * @param patient to save
     */
    @Override
    public String createPatient(Patient patient) {
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
    public String validateEmail(String email) {
        return patientPersistencePort.validateEmail(email);
    }
}
