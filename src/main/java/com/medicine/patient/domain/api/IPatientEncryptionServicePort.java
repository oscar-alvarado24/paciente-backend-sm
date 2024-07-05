package com.medicine.patient.domain.api;

public interface IPatientEncryptionServicePort {
    String encrypt(String word);
    String decrypt(String wordEncrypted);
}
