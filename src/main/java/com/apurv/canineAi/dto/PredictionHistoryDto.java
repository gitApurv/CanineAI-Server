package com.apurv.canineAi.dto;

import java.util.List;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionHistoryDto {
    private String predictionId;
    private String dogName;
    private List<String> matchedSymptomsNames;
    private String predictedDiseaseName;
    private Instant createdAt;
}
