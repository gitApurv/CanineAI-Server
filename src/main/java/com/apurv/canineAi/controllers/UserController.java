package com.apurv.canineAi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.apurv.canineAi.constants.ApiUrls;
import com.apurv.canineAi.dto.ApiResponse;
import com.apurv.canineAi.dto.UserDto;
import com.apurv.canineAi.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(ApiUrls.USER)
    public ResponseEntity<ApiResponse<UserDto>> getUserInfo(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String userId = userIdAttr.toString();
        UserDto user = userService.getUserInfo(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
}