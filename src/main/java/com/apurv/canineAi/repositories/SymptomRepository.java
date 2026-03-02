package com.apurv.canineAi.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apurv.canineAi.models.entity.SymptomEntity;

@Repository
public interface SymptomRepository extends MongoRepository<SymptomEntity, String> {
    List<SymptomEntity> findByNameIn(List<String> names);
}