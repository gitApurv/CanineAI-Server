package com.apurv.canineAi.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apurv.canineAi.models.entity.DogEntity;

@Repository
public interface DogRepository extends MongoRepository<DogEntity, String> {
	List<DogEntity> findByOwnerId(String ownerId);
}
