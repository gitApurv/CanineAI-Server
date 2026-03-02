package com.apurv.canineAi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DogSummaryDto {
    private String name;
    private String breed;
    private String profileImageUrl;
}
