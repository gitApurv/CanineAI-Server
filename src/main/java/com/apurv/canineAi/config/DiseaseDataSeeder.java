package com.apurv.canineAi.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.apurv.canineAi.models.entity.DiseaseEntity;
import com.apurv.canineAi.models.enums.DiseaseTags;
import com.apurv.canineAi.models.shared.InfoItem;
import com.apurv.canineAi.repositories.DiseaseRepository;

@Component
public class DiseaseDataSeeder implements CommandLineRunner {

    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DiseaseDataSeeder(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public void run(String... args) {
        if (diseaseRepository.count() > 0) {
            return;
        }

        DiseaseEntity parvovirus = new DiseaseEntity(
                null,
                "Canine Parvovirus",
                new DiseaseTags[] { DiseaseTags.VIRAL },
                "Highly contagious viral disease in puppies and unvaccinated dogs.",
                "https://img.freepik.com/premium-photo/studio-portrait-brown-white-black-medium-mixed-breed-dog-smiling-against-green-background_772412-3654.jpg",
                "Parvovirus primarily attacks the intestinal tract and can cause severe dehydration.",
                List.of(
                        new InfoItem("Vomiting", "Frequent vomiting leading to fluid loss."),
                        new InfoItem("Bloody diarrhea", "Severe diarrhea with blood and foul smell."),
                        new InfoItem("Lethargy", "Dog becomes weak and less responsive.")),
                List.of(
                        new InfoItem("Viral exposure", "Contact with infected feces or contaminated surfaces."),
                        new InfoItem("Low immunity", "Unvaccinated puppies are at highest risk.")),
                List.of(
                        new InfoItem("Vaccination", "Follow core vaccine schedule advised by vet."),
                        new InfoItem("Hygiene", "Disinfect kennels, bowls, and common surfaces regularly.")));

        DiseaseEntity mange = new DiseaseEntity(
                null,
                "Mange",
                new DiseaseTags[] { DiseaseTags.PARASITIC },
                "Skin disease caused by mites, resulting in itching and hair loss.",
                "https://img.freepik.com/premium-photo/studio-portrait-brown-white-black-medium-mixed-breed-dog-smiling-against-green-background_772412-3654.jpg",
                "Different mite species cause sarcoptic or demodectic mange; severity varies by immunity.",
                List.of(
                        new InfoItem("Intense itching", "Persistent scratching and skin irritation."),
                        new InfoItem("Hair loss", "Patchy or generalized fur loss."),
                        new InfoItem("Red skin", "Inflamed skin with crusting in severe cases.")),
                List.of(
                        new InfoItem("Mite infestation", "Transmission through close contact with infected animals."),
                        new InfoItem("Weak immune system", "Can worsen demodectic mange.")),
                List.of(
                        new InfoItem("Regular grooming", "Check skin and coat routinely for early signs."),
                        new InfoItem("Early treatment", "Consult a vet at first signs of severe itching.")));

        DiseaseEntity kennelCough = new DiseaseEntity(
                null,
                "Kennel Cough",
                new DiseaseTags[] { DiseaseTags.VIRAL, DiseaseTags.BACTERIAL },
                "Respiratory infection common in social dog environments.",
                "https://img.freepik.com/premium-photo/studio-portrait-brown-white-black-medium-mixed-breed-dog-smiling-against-green-background_772412-3654.jpg",
                "Often caused by a mix of pathogens such as Bordetella and canine parainfluenza.",
                List.of(
                        new InfoItem("Dry cough", "Persistent honking cough, especially after activity."),
                        new InfoItem("Sneezing", "Mild upper respiratory irritation."),
                        new InfoItem("Nasal discharge", "Watery or mucous discharge from nose.")),
                List.of(
                        new InfoItem("Crowded environments", "Exposure in boarding, shelters, or parks."),
                        new InfoItem("Airborne spread", "Transmitted through respiratory droplets.")),
                List.of(
                        new InfoItem("Bordetella vaccine", "Vaccinate dogs with frequent social exposure."),
                        new InfoItem("Isolation", "Keep symptomatic dogs away from healthy dogs.")));

        diseaseRepository.saveAll(List.of(parvovirus, mange, kennelCough));
    }
}
