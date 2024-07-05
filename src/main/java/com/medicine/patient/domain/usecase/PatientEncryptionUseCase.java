package com.medicine.patient.domain.usecase;

import com.medicine.patient.domain.api.IPatientEncryptionServicePort;
import com.medicine.patient.domain.spi.IPatientEncryptionPersistencePort;

public class PatientEncryptionUseCase implements IPatientEncryptionServicePort {
    private final IPatientEncryptionPersistencePort patientEncryptionPersistancePort;

    public PatientEncryptionUseCase(IPatientEncryptionPersistencePort patientEncryptionPersistancePort) {
        this.patientEncryptionPersistancePort = patientEncryptionPersistancePort;
    }

    @Override
    public String encrypt(String word) {
        return this.patientEncryptionPersistancePort.encrypt(word);
    }

    @Override
    public String decrypt(String wordEncrypted) {
        return this.patientEncryptionPersistancePort.decrypt(wordEncrypted);
    }
}
