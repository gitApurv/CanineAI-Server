package com.apurv.canineAi.dto;

import com.apurv.canineAi.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DogRequestDto {
	private String name;
	private String breed;
	private Long ageYears;
	private Long weightKg;
	private Gender gender;
	private Boolean vaccinated;
	private String profileImageUrl;
}
