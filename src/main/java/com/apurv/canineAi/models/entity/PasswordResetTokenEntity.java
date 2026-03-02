package com.apurv.canineAi.models.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_reset_tokens")
public class PasswordResetTokenEntity {

    @Id
    private String id;
    private String userId;
    private String email;
    private String tokenHash;
    private Instant expiresAt;
    private Instant createdAt;
}