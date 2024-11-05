package com.colombia.eps.patient.infrastructure.encryption.adapter;

import com.colombia.eps.patient.domain.spi.IPatientEncryptionPersistencePort;
import com.colombia.eps.patient.infrastructure.encryption.systemencryption.Encryption;
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
