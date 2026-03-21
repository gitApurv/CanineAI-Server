package com.apurv.canineAi.services;

import org.springframework.stereotype.Service;

@Service
public class PredictionDiseaseResolverService {

    private final DiseaseService diseaseService;

    public PredictionDiseaseResolverService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    public String resolvePredictedDiseaseId(String predictedDiseaseName) {
        if (predictedDiseaseName == null || predictedDiseaseName.isBlank()) {
            throw new RuntimeException("Model response does not contain predicted disease information");
        }

        return diseaseService.getDiseaseIdByName(predictedDiseaseName)
                .orElseThrow(() -> new RuntimeException(
                        "Unable to resolve predicted disease: " + predictedDiseaseName));
    }
}
