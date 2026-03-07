package com.apurv.canineAi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtTokenUtil {

    private static final Properties APP_PROPERTIES = loadApplicationProperties();

    private JwtTokenUtil() {
    }

    public static String sign(String userId) {
        Instant now = Instant.now();
        String issuer = getIssuer();
        long expirationSeconds = getExpirationSeconds();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userId)
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(expirationSeconds))
                .sign(getAlgorithm());
    }

    public static DecodedJWT verify(String token) {
        String issuer = getIssuer();
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }

    private static Algorithm getAlgorithm() {
        String secret = getSecretString();
        return Algorithm.HMAC256(secret);
    }

    private static String getIssuer() {
        return requireProperty("jwt.issuer");
    }

    private static long getExpirationSeconds() {
        String rawValue = requireProperty("jwt.expiration-seconds");
        try {
            return Long.parseLong(rawValue);
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("Property 'jwt.expiration-seconds' must be a valid long value", ex);
        }
    }

    private static String getSecretString() {
        return requireProperty("jwt.secret");
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
        try (InputStream inputStream = JwtTokenUtil.class.getClassLoader()
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
