package com.apurv.canineAi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {
    private String fullName;
    private String email;
    private String password;
    private String profilePictureUrl;
}
