package com.medicine.patient.domain.spi;

public interface IPatientEncryptionPersistencePort {
    String encrypt(String word);
    String decrypt(String wordEncrypted);
}
