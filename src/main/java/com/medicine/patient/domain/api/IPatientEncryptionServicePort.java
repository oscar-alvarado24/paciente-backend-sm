package com.medicine.register.domain.api;

public interface IPatientEncryptionServicePort {
    String encrypt(String word);
    String decrypt(String wordEncrypted);
}
