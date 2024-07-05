package com.medicine.register.domain.usecase;

import com.medicine.register.domain.api.IPatientEncryptionServicePort;
import com.medicine.register.domain.spi.IPatientEncryptionPersistencePort;

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
