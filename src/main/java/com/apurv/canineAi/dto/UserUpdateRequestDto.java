package com.apurv.canineAi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String profilePictureUrl;
}