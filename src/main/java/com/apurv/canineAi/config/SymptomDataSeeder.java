package com.apurv.canineAi.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.apurv.canineAi.models.entity.SymptomEntity;
import com.apurv.canineAi.repositories.SymptomRepository;

@Component
@Order(1)
public class SymptomDataSeeder implements CommandLineRunner {

    private final SymptomRepository symptomRepository;

    @Autowired
    public SymptomDataSeeder(SymptomRepository symptomRepository) {
        this.symptomRepository = symptomRepository;
    }

    @Override
    public void run(String... args) {
        if (symptomRepository.count() > 0) {
            return;
        }

        List<SymptomEntity> symptoms = List.of(
                new SymptomEntity(null, "Vomiting", "Frequent vomiting leading to fluid loss."),
                new SymptomEntity(null, "Bloody diarrhea", "Severe diarrhea with blood and foul smell."),
                new SymptomEntity(null, "Lethargy", "Dog becomes weak and less responsive."),
                new SymptomEntity(null, "Intense itching", "Persistent scratching and skin irritation."),
                new SymptomEntity(null, "Hair loss", "Patchy or generalized fur loss."),
                new SymptomEntity(null, "Red skin", "Inflamed skin with crusting in severe cases."),
                new SymptomEntity(null, "Dry cough", "Persistent honking cough, especially after activity."),
                new SymptomEntity(null, "Sneezing", "Mild upper respiratory irritation."),
                new SymptomEntity(null, "Nasal discharge", "Watery or mucous discharge from nose."));

        symptomRepository.saveAll(symptoms);
    }
}