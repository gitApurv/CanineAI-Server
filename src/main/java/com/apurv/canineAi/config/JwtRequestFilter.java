package com.apurv.canineAi.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.apurv.canineAi.utils.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTH_COOKIE_NAME = "auth_token";
    private static final String USER_ID_ATTRIBUTE = "userId";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/diseases") || path.startsWith("/api/diseases/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie authCookie = WebUtils.getCookie(request, AUTH_COOKIE_NAME);

        if (authCookie != null && authCookie.getValue() != null && !authCookie.getValue().isBlank()) {
            try {
                DecodedJWT decodedJWT = JwtTokenUtil.verify(authCookie.getValue());
                request.setAttribute(USER_ID_ATTRIBUTE, decodedJWT.getSubject());
            } catch (Exception ex) {
            }
        }

        filterChain.doFilter(request, response);
    }
}
