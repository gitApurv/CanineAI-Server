package com.apurv.canineAi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.apurv.canineAi.constants.ApiUrls;
import com.apurv.canineAi.dto.UserLoginRequestDto;
import com.apurv.canineAi.dto.UserRegisterRequestDto;
import com.apurv.canineAi.dto.UserResponseDto;

import com.apurv.canineAi.services.AuthService;
import com.apurv.canineAi.utils.AuthCookieUtil;
import com.apurv.canineAi.utils.JwtTokenUtil;

@RestController
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(ApiUrls.REGISTER_USER)
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        try {
            UserResponseDto registeredUser = authService.registerUser(userRegisterRequestDto);
            String token = JwtTokenUtil.sign(registeredUser.getId());
            ResponseCookie authCookie = AuthCookieUtil.buildAuthCookie(token);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                    .body(registeredUser);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @PostMapping(ApiUrls.LOGIN)
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            UserResponseDto loggedInUser = authService.loginUser(userLoginRequestDto);
            String token = JwtTokenUtil.sign(loggedInUser.getId());
            ResponseCookie authCookie = AuthCookieUtil.buildAuthCookie(token);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                    .body(loggedInUser);
        } catch (IllegalArgumentException ex) {
            if ("Email not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
            }
            if ("Invalid password".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @GetMapping(ApiUrls.LOGOUT)
    public ResponseEntity<?> logout() {
        ResponseCookie authCookie = AuthCookieUtil.clearAuthCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body("Logged out successfully");
    }

}
