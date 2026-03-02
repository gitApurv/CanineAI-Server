package com.apurv.canineAi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender javaMailSender, @Value("${mail.from}") String fromEmail) {
        this.javaMailSender = javaMailSender;
        this.fromEmail = fromEmail;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        if (toEmail == null || toEmail.isBlank()) {
            throw new IllegalArgumentException("Recipient email is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject is required");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Email body is required");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new IllegalStateException("Failed to send email", ex);
        }
    }
}
