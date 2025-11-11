package com.annztech.rewardsystem.common.repsoitory.impl;

import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceGeneratorRepositoryImpl implements SequenceGeneratorRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Long getNextSequence(String sequenceName) {
        String sql = String.format("SELECT nextval('%s')", sequenceName);
        return ((Number) entityManager.createNativeQuery(sql).getSingleResult()).longValue();
    }
}
