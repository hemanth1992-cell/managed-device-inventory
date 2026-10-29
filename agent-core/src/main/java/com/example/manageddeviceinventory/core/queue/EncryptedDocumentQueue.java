package com.example.manageddeviceinventory.core.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {
    public static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int IV_BYTES = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static byte[] createKey() {
        byte[] key = new byte[32];
        SECURE_RANDOM.nextBytes(key);
        return key;
    }

    public static EncryptedPayload encrypt(byte[] key, byte[] plaintext, byte[] associatedData) {
        try {
            byte[] nonce = new byte[IV_BYTES];
            SECURE_RANDOM.nextBytes(nonce);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            if (associatedData != null && associatedData.length > 0) {
                cipher.updateAAD(associatedData);
            }
            byte[] ciphertext = cipher.doFinal(plaintext);
            return new EncryptedPayload(nonce, ciphertext);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt payload", e);
        }
    }

    public static byte[] decrypt(byte[] key, byte[] nonce, byte[] ciphertext, byte[] associatedData) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            if (associatedData != null && associatedData.length > 0) {
                cipher.updateAAD(associatedData);
            }
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt payload", e);
        }
    }

    public static String toBase64(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

    public static byte[] fromBase64(String value) {
        return Base64.getDecoder().decode(value);
    }

    public static class EncryptedPayload {
        private final byte[] nonce;
        private final byte[] cipherText;

        public EncryptedPayload(byte[] nonce, byte[] cipherText) {
            this.nonce = nonce;
            this.cipherText = cipherText;
        }

        public byte[] getNonce() { return nonce; }
        public byte[] getCipherText() { return cipherText; }
    }
}
