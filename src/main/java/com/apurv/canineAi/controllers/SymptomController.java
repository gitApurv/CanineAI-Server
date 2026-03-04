package com.apurv.canineAi.controllers;

import com.apurv.canineAi.constants.ApiUrls;
import com.apurv.canineAi.dto.ApiResponse;
import com.apurv.canineAi.dto.SymptomDto;
import com.apurv.canineAi.repositories.SymptomRepository;
import com.apurv.canineAi.services.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SymptomController {

    private SymptomService symptomService;

    @Autowired
    public SymptomController(SymptomService symptomService) {
        this.symptomService = symptomService;
    }

    @GetMapping(ApiUrls.SYMPTOMS)
    public ResponseEntity<ApiResponse<List<SymptomDto>>> getSymptoms() {
        try {
            List<SymptomDto> symptoms = symptomService.getAllSymptoms();
            return ResponseEntity.ok(ApiResponse.success(symptoms));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.error("Internal server error"));
        }
    }
}
