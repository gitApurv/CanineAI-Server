package com.apurv.canineAi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.apurv.canineAi.constants.ApiUrls;
import com.apurv.canineAi.dto.DiseaseDto;
import com.apurv.canineAi.dto.DiseaseSummaryDto;
import com.apurv.canineAi.services.DiseaseService;

@RestController
public class DiseaseController {

    private final DiseaseService diseaseService;

    @Autowired
    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @GetMapping(ApiUrls.DISEASES)
    public ResponseEntity<List<DiseaseSummaryDto>> getDiseases() {
        List<DiseaseSummaryDto> diseases = diseaseService.getAllDiseases();
        return ResponseEntity.ok(diseases);
    }

    @GetMapping(ApiUrls.DISEASES + "/{id}")
    public ResponseEntity<DiseaseDto> getDiseaseById(@PathVariable String id) {
        DiseaseDto disease = diseaseService.getDiseaseById(id);
        if (disease != null) {
            return ResponseEntity.ok(disease);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
