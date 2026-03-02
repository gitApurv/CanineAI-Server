package com.apurv.canineAi.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.apurv.canineAi.models.entity.DiseaseEntity;
import com.apurv.canineAi.models.entity.SymptomEntity;
import com.apurv.canineAi.models.enums.DiseaseTags;
import com.apurv.canineAi.models.shared.InfoItem;
import com.apurv.canineAi.repositories.DiseaseRepository;
import com.apurv.canineAi.repositories.SymptomRepository;

@Component
@Order(2)
public class DiseaseDataSeeder implements CommandLineRunner {

    private final DiseaseRepository diseaseRepository;
        private final SymptomRepository symptomRepository;

    @Autowired
        public DiseaseDataSeeder(DiseaseRepository diseaseRepository, SymptomRepository symptomRepository) {
        this.diseaseRepository = diseaseRepository;
                this.symptomRepository = symptomRepository;
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
                getSymptomsByNames("Vomiting", "Bloody diarrhea", "Lethargy"),
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
                getSymptomsByNames("Intense itching", "Hair loss", "Red skin"),
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
                getSymptomsByNames("Dry cough", "Sneezing", "Nasal discharge"),
                List.of(
                        new InfoItem("Crowded environments", "Exposure in boarding, shelters, or parks."),
                        new InfoItem("Airborne spread", "Transmitted through respiratory droplets.")),
                List.of(
                        new InfoItem("Bordetella vaccine", "Vaccinate dogs with frequent social exposure."),
                        new InfoItem("Isolation", "Keep symptomatic dogs away from healthy dogs.")));

        diseaseRepository.saveAll(List.of(parvovirus, mange, kennelCough));
    }

        private List<String> getSymptomsByNames(String... names) {
                List<String> requestedNames = List.of(names);
                List<SymptomEntity> matchedSymptoms = symptomRepository.findByNameIn(requestedNames);

                Map<String, SymptomEntity> byName = matchedSymptoms.stream()
                                .collect(Collectors.toMap(SymptomEntity::getName, Function.identity()));

                return requestedNames.stream()
                                .map(name -> {
                                        SymptomEntity symptom = byName.get(name);
                                        if (symptom == null) {
                                                throw new IllegalStateException("Missing seeded symptom: " + name);
                                        }
                                        return symptom.getId();
                                })
                                .toList();
        }
}
