package com.medicine.patient.domain.usecase;

import com.medicine.patient.domain.api.IPatientServicePort;
import com.medicine.patient.domain.model.Patient;
import com.medicine.patient.domain.spi.IPatientPersistencePort;

public class PatientUseCase implements IPatientServicePort {
    private final IPatientPersistencePort patientPersistencePort;

    public PatientUseCase(IPatientPersistencePort patientPersistencePort) {
        this.patientPersistencePort = patientPersistencePort;
    }

    /**
     * @param patient to save
     */
    @Override
    public String createPatient(Patient patient) {
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
