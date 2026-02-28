package com.apurv.canineAi.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHasherUtil {

    private PasswordHasherUtil() {
    }

    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            return false;
        }
        if (hashedPassword == null || hashedPassword.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
