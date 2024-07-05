package com.medicine.register.domain.spi;

public interface IPatientEncryptionPersistencePort {
    String encrypt(String word);
    String decrypt(String wordEncrypted);
}
