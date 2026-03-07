package com.apurv.canineAi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.http.ResponseCookie;

public class AuthCookieUtil {

    private static final Properties APP_PROPERTIES = loadApplicationProperties();

    private AuthCookieUtil() {
    }

    public static ResponseCookie buildAuthCookie(String token) {
        return ResponseCookie.from("auth_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(getExpirationSeconds())
                .build();
    }

    public static ResponseCookie clearAuthCookie() {
        return ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();
    }

    private static long getExpirationSeconds() {
        String rawValue = requireProperty("jwt.expiration-seconds");
        try {
            return Long.parseLong(rawValue);
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("Property 'jwt.expiration-seconds' must be a valid long value", ex);
        }
    }

    private static String requireProperty(String key) {
        String value = APP_PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value;
    }

    private static Properties loadApplicationProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = AuthCookieUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load application.properties", ex);
        }
        return properties;
    }
}