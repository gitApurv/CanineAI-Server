package com.apurv.canineAi.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.DiseaseDto;
import com.apurv.canineAi.dto.DiseaseSummaryDto;
import com.apurv.canineAi.dto.SymptomDto;
import com.apurv.canineAi.models.entity.DiseaseEntity;
import com.apurv.canineAi.models.entity.SymptomEntity;
import com.apurv.canineAi.repositories.DiseaseRepository;
import com.apurv.canineAi.repositories.SymptomRepository;

@Service
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final SymptomRepository symptomRepository;

    @Autowired
    public DiseaseService(DiseaseRepository diseaseRepository, SymptomRepository symptomRepository) {
        this.diseaseRepository = diseaseRepository;
        this.symptomRepository = symptomRepository;
    }

    public List<DiseaseSummaryDto> getAllDiseases() {
        return diseaseRepository.findAll()
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    public DiseaseDto getDiseaseById(String id) {
        return diseaseRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    private DiseaseDto toDto(DiseaseEntity diseaseEntity) {
        List<SymptomDto> symptomItems = mapSymptomIdsToDtos(diseaseEntity.getSymptoms());

        return new DiseaseDto(
                diseaseEntity.getId(),
                diseaseEntity.getName(),
                diseaseEntity.getTags(),
                diseaseEntity.getShortDescription(),
                diseaseEntity.getDiseaseImageUrl(),
                diseaseEntity.getOverview(),
                symptomItems,
                diseaseEntity.getCauses(),
                diseaseEntity.getPreventionTips());
    }

    private List<SymptomDto> mapSymptomIdsToDtos(List<String> symptomIds) {
        if (symptomIds == null || symptomIds.isEmpty()) {
            return List.of();
        }

        List<SymptomEntity> matchedSymptoms = symptomRepository.findAllById(symptomIds);
        Map<String, SymptomEntity> byId = matchedSymptoms.stream()
                .collect(Collectors.toMap(SymptomEntity::getId, Function.identity()));

        return symptomIds.stream()
                .map(id -> {
                    SymptomEntity symptom = byId.get(id);
                    if (symptom == null) {
                        throw new IllegalStateException("Missing symptom for id: " + id);
                    }
                    return new SymptomDto(symptom.getName(), symptom.getDescription());
                })
                .toList();
    }

    private DiseaseSummaryDto toSummaryDto(DiseaseEntity diseaseEntity) {
        return new DiseaseSummaryDto(
                diseaseEntity.getId(),
                diseaseEntity.getName(),
                diseaseEntity.getShortDescription(),
                diseaseEntity.getTags());
    }
}
