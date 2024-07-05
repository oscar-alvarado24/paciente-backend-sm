package com.medicine.patient.infrastructure.encryption.adapter;

import com.medicine.patient.domain.spi.IPatientEncryptionPersistencePort;
import com.medicine.patient.infrastructure.encryption.systemEncryption.Encryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PatientEncryptionAdapter implements IPatientEncryptionPersistencePort {
    private final Encryption encryption;
    @Override
    public String encrypt(String word) {

        return this.encryption.encryptWithPublicKey(word);
    }

    @Override
    public String decrypt(String wordEncrypted) {
        return this.encryption.decryptWithPrivateKey(wordEncrypted);
    }
}
