package com.apurv.canineAi.dto;

import java.util.List;

import com.apurv.canineAi.models.enums.SeverityLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictRequestDto {
    private String dogId;
    private List<String> symptoms;
    private SeverityLevel severityLevel;
    private Long symptomsDuration;
}
