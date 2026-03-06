package com.apurv.canineAi.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionLatestDto {
    private String dogName;
    private String predictedDiseaseName;
    private Instant createdAt;
}
