package com.apurv.canineAi.controllers;

import com.apurv.canineAi.dto.ApiResponse;
import com.apurv.canineAi.dto.DogPredictionDto;
import com.apurv.canineAi.dto.PredictRequestDto;
import com.apurv.canineAi.dto.PredictResponseDto;
import com.apurv.canineAi.dto.PredictionHistoryDto;
import com.apurv.canineAi.dto.PredictionHistoryTop3Dto;
import com.apurv.canineAi.dto.PredictionLatestDto;
import com.apurv.canineAi.services.PredictionService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.apurv.canineAi.constants.ApiUrls;
import com.apurv.canineAi.dto.PredictionResponseDto;

@RestController
public class PredictionController {

    private PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping(ApiUrls.PREDICT)
    public ResponseEntity<ApiResponse<PredictResponseDto>> predictDisease(
            HttpServletRequest request,
            @RequestBody PredictRequestDto predictRequest) {

        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            PredictResponseDto response = predictionService.predictDisease(userIdAttr.toString(), predictRequest);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Prediction failed: " + e.getMessage()));
        }
    }

    @GetMapping(ApiUrls.PREDICT + "/{predictionId}")
    public ResponseEntity<ApiResponse<PredictionResponseDto>> getPrediction(HttpServletRequest request,
            @PathVariable String predictionId) {

        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            PredictionResponseDto prediction = predictionService.getPredictionById(userIdAttr.toString(), predictionId);
            return ResponseEntity.ok(ApiResponse.success(prediction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve prediction: " + e.getMessage()));
        }
    }

    @GetMapping(ApiUrls.PREDICT)
    public ResponseEntity<ApiResponse<List<PredictionHistoryDto>>> getAllPredictions(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            List<PredictionHistoryDto> predictions = predictionService.getAllPredictionsForUser(userIdAttr.toString());
            return ResponseEntity.ok(ApiResponse.success(predictions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve predictions: " +
                            e.getMessage()));
        }
    }

    @GetMapping(ApiUrls.PREDICT + "/top3")
    public ResponseEntity<ApiResponse<List<PredictionHistoryTop3Dto>>> getTop3Predictions(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            List<PredictionHistoryTop3Dto> predictions = predictionService
                    .getTop3PredictionsForUser(userIdAttr.toString());
            return ResponseEntity.ok(ApiResponse.success(predictions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve top 3 predictions: " + e.getMessage()));
        }
    }

    @GetMapping(ApiUrls.PREDICT + "/dog/{dogId}")
    public ResponseEntity<ApiResponse<List<DogPredictionDto>>> getPredictionsByDogId(
            @PathVariable String dogId) {
        try {
            List<DogPredictionDto> predictions = predictionService
                    .getAllPredictionsForDog(dogId);
            return ResponseEntity.ok(ApiResponse.success(predictions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve predictions for dog: " + e.getMessage()));
        }
    }

    @GetMapping(ApiUrls.PREDICT + "/count")
    public ResponseEntity<ApiResponse<Long>> getPredictionCount(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            Long count = predictionService.getPredictionCountForUser(userIdAttr.toString());
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve prediction count: " + e.getMessage()));
        }
    }

    @GetMapping(ApiUrls.PREDICT + "/latest")
    public ResponseEntity<ApiResponse<PredictionLatestDto>> getLatestPrediction(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Unauthorized"));
        }

        try {
            PredictionLatestDto latestPrediction = predictionService.getLatestPredictionForUser(userIdAttr.toString());
            if (latestPrediction == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No predictions found for user"));
            }
            return ResponseEntity.ok(ApiResponse.success(latestPrediction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve latest prediction: " + e.getMessage()));
        }
    }
}
