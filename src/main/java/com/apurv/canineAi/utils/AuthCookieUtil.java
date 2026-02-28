package com.apurv.canineAi.utils;

import org.springframework.http.ResponseCookie;

public class AuthCookieUtil {

    private AuthCookieUtil() {
    }

    public static ResponseCookie buildAuthCookie(String token) {
        return ResponseCookie.from("auth_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
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
}
