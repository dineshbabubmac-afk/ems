package com.annztech.rewardsystem.modules.certificate.approval.assignment.repository;
import com.annztech.rewardsystem.modules.certificate.approval.assignment.entity.CertificateCommittee;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CertificateCommitteeRepository extends JpaRepository<CertificateCommittee, UUID> {
    List<CertificateCommittee> findAllByMemberRoleCode_Code(String committeeMember);

    @Query("SELECT e FROM CertificateCommittee e " +
            "WHERE (LOWER(e.member.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.member.email) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND e.memberRoleCode.code = :roleCode")
    List<CertificateCommittee> searchCommitteeEmployee(@Param("query") String query, @Param("roleCode") String roleCode);

    @Query("SELECT e FROM CertificateCommittee e " +
            "WHERE (LOWER(e.member.employeeCode) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.member.department.nameEn) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.member.department.nameAr) LIKE LOWER(CONCAT('%', :query, '%'))) "+
            "AND e.memberRoleCode.code = :roleCode")
    List<CertificateCommittee> searchCommitteeEmployeeByDepartmentOrEmployeeId(@Param("query") String query, @Param("roleCode") String roleCode);

    @Query(
            "SELECT e FROM Employee e WHERE e.id NOT IN (SELECT c.member.id FROM CertificateCommittee c) " +
                    "AND (:query IS NULL " +
                    "OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%')))"
    )
    List<Employee> searchNonCommitteeEmployees(@Param("query") String query);

    Optional<CertificateCommittee> findByMember_id(UUID memberId);

    @Query("""
        SELECT COUNT(c)
        FROM CertificateCommittee c
        WHERE c.memberRoleCode.code = 'HOC'
          AND (:month IS NULL OR EXTRACT(MONTH FROM c.createdAt) = :month)
          AND (:year IS NULL OR EXTRACT(YEAR FROM c.createdAt) = :year)
    """)
    Long countCommitteeHeadsByMonthYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("""
        SELECT COUNT(c)
        FROM CertificateCommittee c
        WHERE c.memberRoleCode.code = 'COMMITTEE_MEMBER'
          AND (:month IS NULL OR EXTRACT(MONTH FROM c.createdAt) = :month)
          AND (:year IS NULL OR EXTRACT(YEAR FROM c.createdAt) = :year)
    """)
    Long countCommitteeMembersByMonthYear(@Param("month") Integer month, @Param("year") Integer year);


}
