package com.apurv.canineAi.controllers;

import com.apurv.canineAi.services.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PredictionController {

    private PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }
}
