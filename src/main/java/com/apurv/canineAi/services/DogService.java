package com.apurv.canineAi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.DogDetailDto;
import com.apurv.canineAi.dto.DogRequestDto;
import com.apurv.canineAi.dto.DogSummaryDto;
import com.apurv.canineAi.models.entity.DogEntity;
import com.apurv.canineAi.repositories.DogRepository;

@Service
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public void addDog(DogRequestDto dogRequest, String userId) {
        DogEntity dog = new DogEntity();
        dog.setOwnerId(userId);
        dog.setName(dogRequest.getName());
        dog.setBreed(dogRequest.getBreed());
        dog.setAgeYears(dogRequest.getAgeYears());
        dog.setWeightKg(dogRequest.getWeightKg());
        dog.setGender(dogRequest.getGender());
        dog.setVaccinated(dogRequest.getVaccinated());
        dog.setProfileImageUrl(dogRequest.getProfileImageUrl());
        dogRepository.save(dog);
    }

    public List<DogSummaryDto> getDogsByOwnerId(String ownerId) {
        return dogRepository.findByOwnerId(ownerId)
                .stream()
                .map(dog -> new DogSummaryDto(
                        dog.getId(),
                        dog.getName(),
                        dog.getBreed(),
                        dog.getProfileImageUrl()))
                .collect(Collectors.toList());
    }

    public DogDetailDto getDogByIdForOwner(String dogId, String userId) {
        DogEntity dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found"));

        if (!userId.equals(dog.getOwnerId())) {
            throw new IllegalArgumentException("Unauthorized access to dog");
        }

        return new DogDetailDto(
                dog.getName(),
                dog.getBreed(),
                dog.getAgeYears(),
                dog.getWeightKg(),
                dog.getGender(),
                dog.getVaccinated(),
                dog.getProfileImageUrl());
    }

    public void updateDog(String dogId, DogRequestDto dogRequest, String userId) {
        DogEntity dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found"));

        if (!userId.equals(dog.getOwnerId())) {
            throw new IllegalArgumentException("Unauthorized access to dog");
        }

        dog.setName(dogRequest.getName());
        dog.setBreed(dogRequest.getBreed());
        dog.setAgeYears(dogRequest.getAgeYears());
        dog.setWeightKg(dogRequest.getWeightKg());
        dog.setGender(dogRequest.getGender());
        dog.setVaccinated(dogRequest.getVaccinated());
        dog.setProfileImageUrl(dogRequest.getProfileImageUrl());

        dogRepository.save(dog);
    }

    public void deleteDog(String dogId, String userId) {
        DogEntity dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found"));

        if (!userId.equals(dog.getOwnerId())) {
            throw new IllegalArgumentException("Unauthorized access to dog");
        }

        dogRepository.delete(dog);
    }
}
