package com.colombia.eps.patient.application.helper;

import com.colombia.eps.patient.application.helper.exception.CryptoUtilException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CryptoUtil {

    private static final String SECRET_KEY ;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    static {
        // ✅ Lee desde System.getenv() en MAYÚSCULAS
        SECRET_KEY = System.getenv("SECRET_KEY");

        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            log.error("❌ FATAL: La variable de entorno 'SECRET_KEY' no está configurada");
            throw new ExceptionInInitializerError(
                    "La variable de entorno 'SECRET_KEY' es requerida. " +
                            "Verifica el parámetro 'secret_key' en SSM Parameter Store."
            );
        }

        // Valida que sea Base64 válido
        try {
            byte[] decoded = Base64.getDecoder().decode(SECRET_KEY);
            if (decoded.length != 16 && decoded.length != 24 && decoded.length != 32) {
                log.error("❌ SECRET_KEY tiene {} bytes. Debe ser 16, 24 o 32 bytes", decoded.length);
                throw new ExceptionInInitializerError(
                        String.format("SECRET_KEY debe ser AES válido (128, 192 o 256 bits). Actual: %d bytes", decoded.length)
                );
            }
            log.info("✅ CryptoUtil inicializado con clave AES de {} bits", decoded.length * 8);
        } catch (IllegalArgumentException e) {
            log.error("❌ SECRET_KEY no es Base64 válido: {}", e.getMessage());
            throw new ExceptionInInitializerError("SECRET_KEY debe estar en formato Base64 válido");
        }
    }
    public static String encrypt(String data)  {
        try {
            SecretKeySpec key = generateKey();

            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ApplicationConstants.ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

            return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
        } catch (Exception exception){
            log.error("Error al encriptar el dato: {}", exception.getMessage());
            throw new CryptoUtilException(String.format(ApplicationConstants.MSG_ERROR_CRYPTO,ApplicationConstants.ENCRYPT));
        }
    }

    public static String decrypt(String encryptedData) {
        try {
        SecretKeySpec key = generateKey();
        String standard = encryptedData
                .replace('-', '+')
                .replace('_', '/');

        int padding = (4 - (standard.length() % 4)) % 4;
        standard += "=".repeat(padding);
        byte[] decodedData = Base64.getDecoder().decode(standard);

        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(decodedData);

        byte[] iv = new byte[GCM_IV_LENGTH];
        buffer.get(iv);

        byte[] ciphertext = new byte[buffer.remaining()];
        buffer.get(ciphertext);

        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        Cipher cipher = Cipher.getInstance(ApplicationConstants.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decryptedData = cipher.doFinal(ciphertext);

        return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception exception){
            log.error("Error al desencriptar el dato: {}", exception.getMessage());
            throw new CryptoUtilException(String.format(ApplicationConstants.MSG_ERROR_CRYPTO,ApplicationConstants.DECRYPT));
        }
    }

    private static SecretKeySpec generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
