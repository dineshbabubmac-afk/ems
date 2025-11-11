package com.annztech.rewardsystem.modules.invite.repository;

import com.annztech.rewardsystem.modules.invite.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    List<Invite> findInvitesByReferenceId(UUID referenceId);

    Optional<Invite> findInvitesByReferenceIdAndId(UUID uuid, UUID uuid1);
}
