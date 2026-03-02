package com.apurv.canineAi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.apurv.canineAi.models.entity.UserEntity;
import com.apurv.canineAi.services.EmailService;

@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private final EmailService emailService;
    private final String clientUrl;

    public EmailUtil(EmailService emailService, @Value("${client.url}") String clientUrl) {
        this.emailService = emailService;
        this.clientUrl = clientUrl;
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
        String body = "Hi " + userEntity.getFullName() + ",\n\n"
                + "Welcome to CanineAI. Your account has been created successfully.\n\n"
                + "We are happy to have you on board.\n\n"
                + "- CanineAI Team";

        sendEmail(userEntity.getEmail(), subject, body);
    }

    public void sendPasswordResetEmail(UserEntity userEntity, String token) {
        String subject = "Reset your CanineAI password";
        String body = "Hi " + userEntity.getFullName() + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Click the link below to continue:\n\n"
                + clientUrl + "/reset-password?token=" + token + "&email=" + userEntity.getEmail() + "\n\n"
                + "If you did not request this, you can safely ignore this email.\n\n"
                + "- CanineAI Team";

        sendEmail(userEntity.getEmail(), subject, body);
    }

    
}