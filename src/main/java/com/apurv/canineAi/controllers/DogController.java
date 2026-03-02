package com.apurv.canineAi.controllers;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apurv.canineAi.dto.ApiResponse;
import com.apurv.canineAi.dto.DogDetailDto;
import com.apurv.canineAi.services.DogService;
import com.apurv.canineAi.dto.DogRequestDto;
import com.apurv.canineAi.dto.DogSummaryDto;
import com.apurv.canineAi.constants.ApiUrls;

@RestController
public class DogController {
    
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping(ApiUrls.DOGS)
    public ResponseEntity<ApiResponse<String>> addDog(@RequestBody DogRequestDto dogRequest, HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String userId = userIdAttr.toString();
        dogService.addDog(dogRequest, userId);
        return ResponseEntity.ok(ApiResponse.success("Dog added successfully"));
    }

    @GetMapping(ApiUrls.DOGS)
    public ResponseEntity<ApiResponse<List<DogSummaryDto>>> getDogs(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String userId = userIdAttr.toString();
        List<DogSummaryDto> dogs = dogService.getDogsByOwnerId(userId);
        return ResponseEntity.ok(ApiResponse.success(dogs));
    }

    @GetMapping(ApiUrls.DOGS + "/{dogId}")
    public ResponseEntity<ApiResponse<DogDetailDto>> getDogById(HttpServletRequest request, @PathVariable String dogId) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String userId = userIdAttr.toString();
        try {
            DogDetailDto dog = dogService.getDogByIdForOwner(dogId, userId);
            return ResponseEntity.ok(ApiResponse.success(dog));
        } catch (IllegalArgumentException ex) {
            if ("Dog not found".equals(ex.getMessage())) {
                return ResponseEntity.status(404).body(ApiResponse.error("Dog not found"));
            }
            if ("Unauthorized access to dog".equals(ex.getMessage())) {
                return ResponseEntity.status(403).body(ApiResponse.error("Forbidden"));
            }
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping(ApiUrls.DOGS + "/{dogId}")
    public ResponseEntity<ApiResponse<String>> updateDog(HttpServletRequest request, @PathVariable String dogId, @RequestBody DogRequestDto dogRequest) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String userId = userIdAttr.toString();
        try {
            dogService.updateDog(dogId, dogRequest, userId);
            return ResponseEntity.ok(ApiResponse.success("Dog updated successfully"));
        } catch (IllegalArgumentException ex) {
            if ("Dog not found".equals(ex.getMessage())) {
                return ResponseEntity.status(404).body(ApiResponse.error("Dog not found"));
            }
            if ("Unauthorized access to dog".equals(ex.getMessage())) {
                return ResponseEntity.status(403).body(ApiResponse.error("Forbidden"));
            }
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
