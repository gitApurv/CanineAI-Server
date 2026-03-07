package com.apurv.canineAi.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DogPredictionDto {
    private String predictionId;
    private List<String> matchedSymptomsName;
    private String predictedDiseaseName;
    private Instant createdAt;
}
