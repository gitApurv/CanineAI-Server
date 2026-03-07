package com.apurv.canineAi.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apurv.canineAi.models.entity.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetTokenEntity, String> {
    void deleteByEmail(String email);

    Optional<PasswordResetTokenEntity> findByTokenHashAndEmail(String tokenHash, String email);

    void deleteByTokenHash(String tokenHash);
}