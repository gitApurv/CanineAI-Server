package com.apurv.canineAi.utils;

import java.util.Set;
import java.util.Locale;

import com.apurv.canineAi.dto.DogDetailDto;

public class PredictionModelUtil {

    private PredictionModelUtil() {
    }

    public static ModelPredictRequest buildModelRequestBody(DogDetailDto dog, Set<String> normalizedSymptoms) {
        return new ModelPredictRequest(
                dog.getAgeYears(),
                toBreedSize(dog.getWeightKg()),
                toYesNo(dog.getVaccinated()),
                toYesNo(normalizedSymptoms.contains("appetite_loss")),
                toYesNo(normalizedSymptoms.contains("vomiting")),
                toYesNo(normalizedSymptoms.contains("diarrhea")),
                toYesNo(normalizedSymptoms.contains("lethargy")),
                toYesNo(normalizedSymptoms.contains("coughing")),
                toYesNo(normalizedSymptoms.contains("nasal_discharge")),
                toYesNo(normalizedSymptoms.contains("weight_loss")),
                toYesNo(normalizedSymptoms.contains("excessive_salivation")),
                toYesNo(normalizedSymptoms.contains("seizures")));
    }

    public static String normalizeSymptomKey(String symptomName) {
        return symptomName == null ? "" : symptomName.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
    }

    private static String toBreedSize(Long weightKg) {
        if (weightKg == null) {
            return "medium";
        }
        if (weightKg < 10) {
            return "small";
        }
        if (weightKg <= 25) {
            return "medium";
        }
        return "large";
    }

    private static String toYesNo(Boolean value) {
        return Boolean.TRUE.equals(value) ? "yes" : "no";
    }

    public record ModelPredictRequest(
            Long age,
            String breed_size,
            String vaccination_status,
            String appetite_loss,
            String vomiting,
            String diarrhea,
            String lethargy,
            String coughing,
            String nasal_discharge,
            String weight_loss,
            String excessive_salivation,
            String seizures) {
    }

    public record ModelPredictResponse(
            String prediction,
            Double probability,
            String confidence) {
    }
}
