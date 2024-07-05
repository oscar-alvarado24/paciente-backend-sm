package com.medicine.register.infrastructure.encryption.adapter;

import com.medicine.register.domain.spi.IPatientEncryptionPersistencePort;
import com.medicine.register.infrastructure.encryption.systemEncryption.Encryption;
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
