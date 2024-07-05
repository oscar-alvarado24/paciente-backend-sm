package com.medicine.register.infrastructure.encryption.systemEncryption;

import com.medicine.register.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Component
public class Encryption {
    @Value("${alias}")
    protected String alias;
    @Value("${password}")
    protected String keystorePassword;
    @Value("${path}")
    protected String keystorePath;
    @Value("${privateKey.password}")
    protected String privateKeyPassword;


    protected PublicKey getPublicKey() {
       try (FileInputStream fis = new FileInputStream(keystorePath)){
            KeyStore keyStore = KeyStore.getInstance(Constants.TYPE_KEYSTORE);
            keyStore.load(fis, keystorePassword.toCharArray());
            Certificate cert = keyStore.getCertificate(alias);
            return cert.getPublicKey();
        } catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            return null;
        }
    }

    protected PrivateKey getPrivateKey() {
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            KeyStore keyStore = KeyStore.getInstance(Constants.TYPE_KEYSTORE);
            keyStore.load(fis, keystorePassword.toCharArray());

            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(privateKeyPassword.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword);

            return privateKeyEntry.getPrivateKey();
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return null;
        }
    }
    public String encryptWithPublicKey(String text) {
        PublicKey publicKey = getPublicKey();
        try {
            if (publicKey != null) {
                Cipher cipher = Cipher.getInstance(Constants.TRANSFORMATION);
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                byte[] wordEncrypted = cipher.doFinal(text.getBytes());
                return Base64.getEncoder().encodeToString(wordEncrypted);
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            return null;
        }
    }

    public String decryptWithPrivateKey(String wordEncrypted) {
        PrivateKey privateKey = getPrivateKey();
        try {
            Cipher cipher = Cipher.getInstance(Constants.TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] cipherTextBytes = Base64.getDecoder().decode(wordEncrypted);
            byte[] decryptedTextBytes = cipher.doFinal(cipherTextBytes);

            return new String(decryptedTextBytes);
        } catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            return null;
        }
    }
}
