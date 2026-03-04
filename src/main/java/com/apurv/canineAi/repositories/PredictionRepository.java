package com.apurv.canineAi.repositories;

import com.apurv.canineAi.models.entity.PredictionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends MongoRepository<PredictionEntity, String> {
}
