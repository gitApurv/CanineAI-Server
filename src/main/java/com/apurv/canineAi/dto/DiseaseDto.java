package com.apurv.canineAi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.apurv.canineAi.models.enums.DiseaseTags;
import com.apurv.canineAi.models.shared.InfoItem;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDto {
    private String id;
    private String name;
    private DiseaseTags[] tags;
    private String shortDescription;
    private String diseaseImageUrl;
    private String overview;
    private List<InfoItem> symptoms;
    private List<InfoItem> causes;
    private List<InfoItem> preventionTips;
}
