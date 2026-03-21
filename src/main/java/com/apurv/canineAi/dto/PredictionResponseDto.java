package com.apurv.canineAi.dto;

import java.util.List;

import com.apurv.canineAi.models.shared.InfoItem;
import com.apurv.canineAi.models.enums.DiseaseTags;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseDto {
    private String dogName;
    private String predictedDiseaseName;
    private String predictedDiseaseOverview;
    private List<DiseaseTags> predictedDiseaseTags;
    private Double probability;
    private String confidence;
    private List<InfoItem> predictedDiseasePreventionTips;
    private List<SymptomDto> matchedSymptoms;
    private Instant createdAt;
}
