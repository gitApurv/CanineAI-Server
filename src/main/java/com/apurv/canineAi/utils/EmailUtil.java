package com.apurv.canineAi.utils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.apurv.canineAi.models.entity.UserEntity;
import com.apurv.canineAi.services.EmailService;

@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private final EmailService emailService;
    private final List<String> allowedClientUrls;
    private final String defaultClientUrl;

    public EmailUtil(EmailService emailService, @Value("${client.urls:}") String rawClientUrls) {
        this.emailService = emailService;
        this.allowedClientUrls = parseAllowedClientUrls(rawClientUrls);
        this.defaultClientUrl = allowedClientUrls.isEmpty() ? "" : allowedClientUrls.get(0);
    }

    private List<String> parseAllowedClientUrls(String rawClientUrls) {
        if (!StringUtils.hasText(rawClientUrls)) {
            return List.of();
        }

        return Arrays.stream(rawClientUrls.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(this::normalizeUrl)
                .toList();
    }

    private String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private String extractOriginFromReferer(String referer) {
        if (!StringUtils.hasText(referer)) {
            return "";
        }

        try {
            URI refererUri = URI.create(referer.trim());
            if (!StringUtils.hasText(refererUri.getScheme()) || !StringUtils.hasText(refererUri.getHost())) {
                return "";
            }

            int port = refererUri.getPort();
            if (port > -1) {
                return normalizeUrl(refererUri.getScheme() + "://" + refererUri.getHost() + ":" + port);
            }

            return normalizeUrl(refererUri.getScheme() + "://" + refererUri.getHost());
        } catch (IllegalArgumentException ex) {
            logger.debug("Invalid referer header received", ex);
            return "";
        }
    }

    private String resolveClientUrl(String requestOrigin, String requestReferer) {
        if (StringUtils.hasText(requestOrigin)) {
            String normalizedOrigin = normalizeUrl(requestOrigin.trim());
            if (allowedClientUrls.contains(normalizedOrigin)) {
                return normalizedOrigin;
            }
        }

        String refererOrigin = extractOriginFromReferer(requestReferer);
        if (StringUtils.hasText(refererOrigin) && allowedClientUrls.contains(refererOrigin)) {
            return refererOrigin;
        }

        return defaultClientUrl;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            emailService.sendEmail(toEmail, subject, body);
        } catch (RuntimeException ex) {
            logger.warn("Email failed for recipient {}", toEmail, ex);
        }
    }

    public void sendWelcomeEmail(UserEntity userEntity) {
        String subject = "Welcome to CanineAI";
        String body = "Hi " + userEntity.getName() + ",\n\n"
                + "Welcome to CanineAI. Your account has been created successfully.\n\n"
                + "We are happy to have you on board.\n\n"
                + "- CanineAI Team";

        sendEmail(userEntity.getEmail(), subject, body);
    }

    public void sendPasswordResetEmail(UserEntity userEntity, String token, String requestOrigin,
            String requestReferer) {
        String clientUrl = resolveClientUrl(requestOrigin, requestReferer);
        String subject = "Reset your CanineAI password";
        String body = "Hi " + userEntity.getName() + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Click the link below to continue:\n\n"
                + clientUrl + "/reset-password?token=" + token + "&email=" + userEntity.getEmail() + "\n\n"
                + "If you did not request this, you can safely ignore this email.\n\n"
                + "- CanineAI Team";

        sendEmail(userEntity.getEmail(), subject, body);
    }

}