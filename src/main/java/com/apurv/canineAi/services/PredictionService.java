package com.apurv.canineAi.services;

import com.apurv.canineAi.dto.DiseaseDto;
import com.apurv.canineAi.dto.DogDetailDto;
import com.apurv.canineAi.dto.DogPredictionDto;
import com.apurv.canineAi.dto.PredictRequestDto;
import com.apurv.canineAi.dto.PredictResponseDto;
import com.apurv.canineAi.dto.PredictionHistoryDto;
import com.apurv.canineAi.dto.PredictionHistoryTop3Dto;
import com.apurv.canineAi.dto.PredictionLatestDto;
import com.apurv.canineAi.dto.PredictionResponseDto;
import com.apurv.canineAi.dto.SymptomDto;
import com.apurv.canineAi.models.entity.PredictionEntity;
import com.apurv.canineAi.models.enums.SeverityLevel;
import com.apurv.canineAi.repositories.PredictionRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private PredictionRepository predictionRepository;
    private DogService dogService;
    private DiseaseService diseaseService;
    private SymptomService symptomService;

    @Autowired
    public PredictionService(PredictionRepository predictionRepository, DogService dogService,
            DiseaseService diseaseService, SymptomService symptomService) {
        this.predictionRepository = predictionRepository;
        this.dogService = dogService;
        this.diseaseService = diseaseService;
        this.symptomService = symptomService;
    }

    public PredictResponseDto predictDisease(String userId, PredictRequestDto predictionRequest) {
        String dogId = predictionRequest.getDogId();
        List<String> symptoms = predictionRequest.getSymptoms();
        SeverityLevel severityLevel = predictionRequest.getSeverityLevel();
        Long symptomsDuration = predictionRequest.getSymptomsDuration();
        PredictionEntity prediction = new PredictionEntity(null, userId, dogId, symptoms, severityLevel,
                symptomsDuration, "69a5c1a0405c17dcd6cb3b81", Instant.now());
        PredictionEntity savedPrediction = predictionRepository.save(prediction);
        return new PredictResponseDto(savedPrediction.getId());
    }

    public PredictionResponseDto getPredictionById(String userId, String predictionId) {
        PredictionEntity prediction = predictionRepository.findById(predictionId)
                .orElseThrow(() -> new RuntimeException("Prediction not found"));
        DogDetailDto dog = dogService.getDogByIdForOwner(prediction.getDogId(), userId);
        DiseaseDto disease = diseaseService.getDiseaseById(prediction.getPredictedDiseaseId());
        List<SymptomDto> matchedSymptoms = symptomService.mapSymptomIdsToDtos(prediction.getSymptoms());

        return new PredictionResponseDto(dog.getName(), disease.getName(), disease.getOverview(),
                disease.getTags(),
                disease.getPreventionTips(),
                matchedSymptoms,
                prediction.getCreatedAt());
    }

    public List<PredictionHistoryDto> getAllPredictionsForUser(String userId) {
        List<PredictionEntity> predictions = predictionRepository.findByUserId(userId);
        return predictions.stream().map(prediction -> {
            DogDetailDto dog = dogService.getDogByIdForOwner(prediction.getDogId(), userId);
            DiseaseDto disease = diseaseService.getDiseaseById(prediction.getPredictedDiseaseId());
            List<String> matchedSymptomsNames = prediction.getSymptoms().stream().map(symptomId -> {
                SymptomDto symptom = symptomService.getSymptomById(symptomId);
                return symptom.getName();
            }).collect(Collectors.toList());

            return new PredictionHistoryDto(prediction.getId(), dog.getName(), matchedSymptomsNames, disease.getName(),
                    prediction.getCreatedAt());
        }).toList();
    }

    public List<PredictionHistoryTop3Dto> getTop3PredictionsForUser(String userId) {
        List<PredictionEntity> predictions = predictionRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
        return predictions.stream().map(prediction -> {
            DogDetailDto dog = dogService.getDogByIdForOwner(prediction.getDogId(), userId);
            DiseaseDto disease = diseaseService.getDiseaseById(prediction.getPredictedDiseaseId());

            return new PredictionHistoryTop3Dto(prediction.getId(), dog.getName(), disease.getName(),
                    prediction.getCreatedAt());
        }).toList();
    }

    public List<DogPredictionDto> getAllPredictionsForDog(String dogId) {
        List<PredictionEntity> predictions = predictionRepository.findByDogId(dogId);

        return predictions.stream().map(prediction -> {
            DiseaseDto disease = diseaseService.getDiseaseById(prediction.getPredictedDiseaseId());
            List<String> matchedSymptomsNames = prediction.getSymptoms().stream().map(symptomId -> {
                SymptomDto symptom = symptomService.getSymptomById(symptomId);
                return symptom.getName();
            }).collect(Collectors.toList());

            return new DogPredictionDto(prediction.getId(), matchedSymptomsNames, disease.getName(),
                    prediction.getCreatedAt());
        }).toList();
    }

    public Long getPredictionCountForUser(String userId) {
        return predictionRepository.countByUserId(userId);
    }

    public PredictionLatestDto getLatestPredictionForUser(String userId) {

        PredictionEntity prediction = predictionRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        if (prediction == null) {
            return null;
        }

        DogDetailDto dog = dogService.getDogByIdForOwner(prediction.getDogId(), userId);
        DiseaseDto disease = diseaseService.getDiseaseById(prediction.getPredictedDiseaseId());
        return new PredictionLatestDto(dog.getName(), disease.getName(), prediction.getCreatedAt());
    }
}
