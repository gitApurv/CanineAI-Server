package com.apurv.canineAi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.UserLoginRequestDto;
import com.apurv.canineAi.dto.UserRegisterRequestDto;
import com.apurv.canineAi.dto.UserResponseDto;
import com.apurv.canineAi.models.entity.UserEntity;
import com.apurv.canineAi.repositories.UserRepository;
import com.apurv.canineAi.utils.PasswordHasherUtil;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        if (userRepository.findByEmail(userRegisterRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        UserEntity newUser = new UserEntity(
                 null,
                userRegisterRequestDto.getFullName(),
                userRegisterRequestDto.getEmail(),
            PasswordHasherUtil.hash(userRegisterRequestDto.getPassword()),
                userRegisterRequestDto.getProfilePictureUrl());

        UserEntity savedUser = userRepository.save(newUser);
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

    private UserResponseDto toResponseDto(UserEntity userEntity) {
        return new UserResponseDto(userEntity.getId());
    }
}
