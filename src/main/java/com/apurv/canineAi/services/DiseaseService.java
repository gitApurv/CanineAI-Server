package com.apurv.canineAi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.DiseaseDto;
import com.apurv.canineAi.dto.DiseaseSummaryDto;
import com.apurv.canineAi.models.entity.DiseaseEntity;
import com.apurv.canineAi.repositories.DiseaseRepository;

@Service
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DiseaseService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
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
        return new DiseaseDto(
                diseaseEntity.getId(),
                diseaseEntity.getName(),
                diseaseEntity.getTags(),
                diseaseEntity.getShortDescription(),
                diseaseEntity.getDiseaseImageUrl(),
                diseaseEntity.getOverview(),
                diseaseEntity.getSymptoms(),
                diseaseEntity.getCauses(),
                diseaseEntity.getPreventionTips());
    }

    private DiseaseSummaryDto toSummaryDto(DiseaseEntity diseaseEntity) {
        return new DiseaseSummaryDto(
                diseaseEntity.getId(),
                diseaseEntity.getName(),
                diseaseEntity.getShortDescription(),
                diseaseEntity.getTags());
    }
}
