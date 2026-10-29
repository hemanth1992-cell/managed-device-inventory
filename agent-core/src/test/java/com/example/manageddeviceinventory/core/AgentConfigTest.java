package com.example.manageddeviceinventory.core.security;

import com.example.manageddeviceinventory.core.crypto.CryptoUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface SecureKeyProvider {
    byte[] getOrCreateKey();
}

class DefaultSecureKeyProvider implements SecureKeyProvider {
    private final byte[] key;

    public DefaultSecureKeyProvider() {
        this.key = CryptoUtil.createKey();
    }

    @Override
    public byte[] getOrCreateKey() {
        return key;
    }
}
