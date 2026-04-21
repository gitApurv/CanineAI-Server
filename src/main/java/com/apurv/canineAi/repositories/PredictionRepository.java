package com.apurv.canineAi.repositories;

import com.apurv.canineAi.models.entity.PredictionEntity;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends MongoRepository<PredictionEntity, String> {
    List<PredictionEntity> findByUserId(String userId);

    List<PredictionEntity> findByDogId(String dogId);

    void deleteByDogId(String dogId);

    List<PredictionEntity> findTop3ByUserIdOrderByCreatedAtDesc(String userId);

    Long countByUserId(String userId);

    PredictionEntity findTopByUserIdOrderByCreatedAtDesc(String userId);
}
