package com.apurv.canineAi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.apurv.canineAi.constants.ApiUrls;
import com.apurv.canineAi.dto.ApiResponse;
import com.apurv.canineAi.dto.PasswordUpdateRequestDto;
import com.apurv.canineAi.dto.UserDto;
import com.apurv.canineAi.dto.UserUpdateRequestDto;
import com.apurv.canineAi.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(ApiUrls.USER + "/logged-in")
    public ResponseEntity<ApiResponse<Boolean>> ping(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @GetMapping(ApiUrls.USER)
    public ResponseEntity<ApiResponse<UserDto>> getUserInfo(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            String userId = userIdAttr.toString();
            UserDto user = userService.getUserInfo(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error"));
        }
    }

    @PutMapping(ApiUrls.USER)
    public ResponseEntity<ApiResponse<String>> updateUser(HttpServletRequest request,
            @RequestBody UserUpdateRequestDto userRequest) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            userService.updateUserInfo(userIdAttr.toString(), userRequest);
            return ResponseEntity.ok(ApiResponse.success("User information updated successfully"));
        } catch (IllegalArgumentException ex) {
            if ("User not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found"));
            }
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error"));
        }
    }

    @PostMapping(ApiUrls.USER)
    public ResponseEntity<ApiResponse<String>> updatePassword(
            HttpServletRequest request, @RequestBody PasswordUpdateRequestDto passwordUpdateRequest) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            userService.updatePassword(userIdAttr.toString(), passwordUpdateRequest);
            return ResponseEntity.ok(ApiResponse.success("Password updated successfully"));
        } catch (IllegalArgumentException ex) {
            if ("User not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found"));
            }
            if ("Current password is incorrect".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Current password is incorrect"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error"));
        }
    }

}