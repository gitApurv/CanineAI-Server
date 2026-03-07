package com.apurv.canineAi.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.UserLoginRequestDto;
import com.apurv.canineAi.dto.UserRegisterRequestDto;
import com.apurv.canineAi.dto.UserResponseDto;
import com.apurv.canineAi.models.entity.PasswordResetTokenEntity;
import com.apurv.canineAi.models.entity.UserEntity;
import com.apurv.canineAi.repositories.PasswordResetTokenRepository;
import com.apurv.canineAi.repositories.UserRepository;
import com.apurv.canineAi.utils.EmailUtil;
import com.apurv.canineAi.utils.PasswordHasherUtil;
import com.apurv.canineAi.utils.TokenHashUtil;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailUtil emailUtil;
    private final long passwordResetTokenExpirationMinutes;
    
    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailUtil emailUtil,
            @Value("${password-reset.token-expiration-minutes:30}") long passwordResetTokenExpirationMinutes) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailUtil = emailUtil;
        this.passwordResetTokenExpirationMinutes = passwordResetTokenExpirationMinutes;
    }

    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        if (userRepository.findByEmail(userRegisterRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        UserEntity newUser = new UserEntity(
                 null,
                userRegisterRequestDto.getName(),
                userRegisterRequestDto.getEmail(),
            PasswordHasherUtil.hash(userRegisterRequestDto.getPassword()),
                userRegisterRequestDto.getProfilePictureUrl(),
                Instant.now());

        UserEntity savedUser = userRepository.save(newUser);

        emailUtil.sendWelcomeEmail(savedUser);

        return toResponseDto(savedUser);
    }

    public UserResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userLoginRequestDto.getEmail());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Email not found");
        }

        UserEntity user = existingUser.get();
        if (!PasswordHasherUtil.verify(userLoginRequestDto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return toResponseDto(user);
    }

    public void forgotPassword(String email, String requestOrigin, String requestReferer) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Email not found");
        }

        UserEntity user = existingUser.get();
        String token = UUID.randomUUID().toString();
        String tokenHash = TokenHashUtil.sha256(token);
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(passwordResetTokenExpirationMinutes * 60);

        passwordResetTokenRepository.deleteByEmail(user.getEmail());
        passwordResetTokenRepository.save(new PasswordResetTokenEntity(
                null,
                user.getId(),
                user.getEmail(),
            tokenHash,
                expiresAt,
                now));

        emailUtil.sendPasswordResetEmail(user, token, requestOrigin, requestReferer);
    }

    public void resetPassword(String token, String email, String newPassword) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        String tokenHash = TokenHashUtil.sha256(token);

        PasswordResetTokenEntity resetToken = passwordResetTokenRepository.findByTokenHashAndEmail(tokenHash, email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.getExpiresAt() == null || Instant.now().isAfter(resetToken.getExpiresAt())) {
            passwordResetTokenRepository.deleteByTokenHash(tokenHash);
            throw new IllegalArgumentException("Token expired");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));

        user.setPasswordHash(PasswordHasherUtil.hash(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.deleteByTokenHash(tokenHash);
    }

    private UserResponseDto toResponseDto(UserEntity userEntity) {
        return new UserResponseDto(userEntity.getId());
    }
}
