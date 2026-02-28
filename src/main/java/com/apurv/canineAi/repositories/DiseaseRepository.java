package com.apurv.canineAi.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apurv.canineAi.models.entity.DiseaseEntity;

@Repository
public interface DiseaseRepository extends MongoRepository<DiseaseEntity, String> {
}
