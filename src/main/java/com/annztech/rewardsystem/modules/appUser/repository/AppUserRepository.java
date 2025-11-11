package com.annztech.rewardsystem.modules.appUser.repository;

import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByEmail(String email);

    Optional<AppUser> findAppUsersByEmail(String email);

    Optional<AppUser> findAppUsersByEmailAndAppCode(String email, String appCode);
}
