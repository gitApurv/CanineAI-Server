package com.apurv.canineAi.services;

import com.apurv.canineAi.dto.SymptomDto;
import com.apurv.canineAi.models.entity.SymptomEntity;
import com.apurv.canineAi.repositories.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SymptomService {

    private SymptomRepository symptomRepository;

    @Autowired
    public void setSymptomRepository(SymptomRepository symptomRepository) {
        this.symptomRepository = symptomRepository;
    }

    public List<SymptomDto> getAllSymptoms() {
        List<SymptomEntity> symptoms = symptomRepository.findAll();
        return symptoms.stream()
                .map(symptom -> new SymptomDto(symptom.getName(), symptom.getDescription()))
                .toList();
    }

    public SymptomDto getSymptomById(String symptomId) {
        SymptomEntity symptom = symptomRepository.findById(symptomId)
                .orElseThrow(() -> new RuntimeException("Symptom not found for id: " + symptomId));
        return new SymptomDto(symptom.getName(), symptom.getDescription());
    }

    public List<SymptomDto> mapSymptomIdsToDtos(List<String> symptomIds) {
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

}
