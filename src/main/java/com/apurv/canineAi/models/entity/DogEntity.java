package com.apurv.canineAi.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.apurv.canineAi.models.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dogs")
public class DogEntity {
    @Id
    private String id;
    private String ownerId;
    private String name;
    private String breed;
    private Long ageYears;
    private Long weightKg;
    private Gender gender;
    private Boolean vaccinated;
    private String profileImageUrl;
}
