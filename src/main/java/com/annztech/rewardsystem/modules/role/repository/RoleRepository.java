package com.annztech.rewardsystem.modules.role.repository;

import com.annztech.rewardsystem.modules.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("SELECT r FROM Role r WHERE r.code = 'SUPPORT' AND r.scope = 'EMPLOYEE'")
    Optional<Role> findEmpSupportId();

    @Query("SELECT r FROM Role r WHERE r.code = 'SUPPORT' AND r.scope = 'CLIENT'")
    Optional<Role> findClientSupportId();

    Optional<Role> getRoleByCode(String code);

    List<Role> getRoleByScope(String scope);

    @Query("SELECT r FROM Role r WHERE r.code != 'SUPER_ADMIN'")
    List<Role> findAll();

    Role findRoleByCode(String roleClientOwner);

    List<Role> findByCodeInIgnoreCase(List<String> codes);
}
