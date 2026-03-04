package com.apurv.canineAi.models.entity;

import com.apurv.canineAi.models.enums.RiskLevel;
import com.apurv.canineAi.models.enums.SeverityLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "predictions")
public class PredictionEntity {
    @Id
    private String id;
    private String userId;
    private String dogId;
    private List<String> symptoms;
    private SeverityLevel severityLevel;
    private Long symptomsDuration;
    private String predictedDiseaseId;
    private Double confidenceScore;
    private RiskLevel riskLevel;
    private Instant createdAt;

}
