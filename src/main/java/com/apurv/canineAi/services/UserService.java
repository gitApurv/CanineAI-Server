package com.apurv.canineAi.services;

import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.PasswordUpdateRequestDto;
import com.apurv.canineAi.dto.UserDto;
import com.apurv.canineAi.dto.UserUpdateRequestDto;
import com.apurv.canineAi.models.entity.UserEntity;
import com.apurv.canineAi.repositories.UserRepository;
import com.apurv.canineAi.utils.PasswordHasherUtil;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserInfo(String userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null) {
            return null;
        }
        return new UserDto(userEntity.getName(), userEntity.getEmail(), userEntity.getProfilePictureUrl(),
                userEntity.getCreatedAt());
    }

    public void updateUserInfo(String userId, UserUpdateRequestDto userDto) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        userEntity.setName(userDto.getName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setProfilePictureUrl(userDto.getProfilePictureUrl());
        userRepository.save(userEntity);
    }

    public void updatePassword(String userId, PasswordUpdateRequestDto passwordUpdateRequest) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!PasswordHasherUtil.verify(passwordUpdateRequest.getCurrentPassword(), userEntity.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        userEntity.setPasswordHash(PasswordHasherUtil.hash(passwordUpdateRequest.getNewPassword()));
        userRepository.save(userEntity);
    }

}
