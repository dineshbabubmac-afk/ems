package com.annztech.rewardsystem.modules.certificate.approval.roles.repository;

import com.annztech.rewardsystem.modules.certificate.approval.roles.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository <MemberRole, String>{
    Optional<MemberRole> findByCode(String code);
}
