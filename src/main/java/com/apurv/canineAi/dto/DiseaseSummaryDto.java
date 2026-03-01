package com.apurv.canineAi.dto;

import com.apurv.canineAi.models.enums.DiseaseTags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseSummaryDto {
    private String diseaseId;
    private String title;
    private String shortDescription;
    private DiseaseTags[] tags;
}
