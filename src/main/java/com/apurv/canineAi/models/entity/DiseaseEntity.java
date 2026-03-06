package com.apurv.canineAi.models.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.apurv.canineAi.models.enums.DiseaseTags;
import com.apurv.canineAi.models.shared.InfoItem;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "diseases")
public class DiseaseEntity {
    @Id
    private String id;
    private String name;
    private List<DiseaseTags> tags;
    private String shortDescription;
    private String diseaseImageUrl;
    private String overview;
    private List<String> symptoms;
    private List<InfoItem> causes;
    private List<InfoItem> preventionTips;
}
