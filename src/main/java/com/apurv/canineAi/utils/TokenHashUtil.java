package com.apurv.canineAi.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class TokenHashUtil {

    private TokenHashUtil() {
    }

    public static String sha256(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexBuilder = new StringBuilder();
            for (byte byteValue : digest) {
                hexBuilder.append(String.format("%02x", byteValue));
            }
            return hexBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Unable to hash value", ex);
        }
    }
}