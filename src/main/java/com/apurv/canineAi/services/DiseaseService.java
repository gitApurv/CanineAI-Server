package com.apurv.canineAi.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apurv.canineAi.dto.DiseaseDto;
import com.apurv.canineAi.dto.DiseaseSummaryDto;
import com.apurv.canineAi.dto.SymptomDto;
import com.apurv.canineAi.models.entity.DiseaseEntity;
import com.apurv.canineAi.repositories.DiseaseRepository;

@Service
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final SymptomService symptomService;

    @Autowired
    public DiseaseService(DiseaseRepository diseaseRepository, SymptomService symptomService) {
        this.diseaseRepository = diseaseRepository;
        this.symptomService = symptomService;
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

    public Optional<String> getDiseaseIdByName(String name) {
        return diseaseRepository.findByNameIgnoreCase(name)
                .map(DiseaseEntity::getId);
    }

    private DiseaseDto toDto(DiseaseEntity diseaseEntity) {
        List<SymptomDto> symptomItems = symptomService.mapSymptomIdsToDtos(diseaseEntity.getSymptoms());

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

    private DiseaseSummaryDto toSummaryDto(DiseaseEntity diseaseEntity) {
        return new DiseaseSummaryDto(
                diseaseEntity.getId(),
                diseaseEntity.getName(),
                diseaseEntity.getShortDescription(),
                diseaseEntity.getTags());
    }
}
