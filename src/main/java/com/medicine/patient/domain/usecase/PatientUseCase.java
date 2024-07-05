package com.medicine.register.domain.usecase;

import com.medicine.register.application.dto.RequestResponse;
import com.medicine.register.domain.api.IPatientServicePort;
import com.medicine.register.domain.model.Patient;
import com.medicine.register.domain.spi.IPatientPersistencePort;

public class PatientUseCase implements IPatientServicePort {
    private final IPatientPersistencePort patientPersistencePort;

    public PatientUseCase(IPatientPersistencePort patientPersistencePort) {
        this.patientPersistencePort = patientPersistencePort;
    }

    /**
     * @param patient to save
     */
    @Override
    public void createPatient(Patient patient) {
        patientPersistencePort.createPatient(patient);
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
