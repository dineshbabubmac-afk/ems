package com.annztech.rewardsystem.modules.tinyurl.repository;

import com.annztech.rewardsystem.modules.tinyurl.entity.TinyUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TinyUrlRepository extends JpaRepository<TinyUrl, UUID> {
    Optional<TinyUrl> findTinyUrlByShortCode(String shortCode);
}
