package com.annztech.rewardsystem.modules.certificate.rejection.entity;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "certificate_request_rejection")
public class CertificateRequestRejection {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Employee member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "certificate_request_id", nullable = false)
    private CertificateRequest certificateRequest;

    @Column(name = "reason", length = Integer.MAX_VALUE)
    private String reason;

    @Size(max = 20)
    @NotNull
    @Column(name = "status_code", nullable = false, length = 20)
    private String statusCode;

    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

}