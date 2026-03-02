package com.apurv.canineAi.services;

import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.UserDto;
import com.apurv.canineAi.models.entity.UserEntity;
import com.apurv.canineAi.repositories.UserRepository;

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
        return new UserDto(userEntity.getName(), userEntity.getEmail(), userEntity.getProfilePictureUrl(), userEntity.getCreatedAt());
    }
}
