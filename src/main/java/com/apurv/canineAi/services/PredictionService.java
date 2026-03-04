package com.apurv.canineAi.services;

import com.apurv.canineAi.repositories.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private PredictionRepository predictionRepository;

    @Autowired
    public PredictionService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }
}
