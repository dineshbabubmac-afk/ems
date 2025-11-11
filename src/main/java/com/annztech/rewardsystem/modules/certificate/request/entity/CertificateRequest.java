package com.annztech.rewardsystem.modules.certificate.request.entity;

import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "certificate_request")
public class CertificateRequest {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Size(max = 20)
    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nominated_to", nullable = false)
    private Employee nominatedTo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nominated_by", nullable = false)
    private Employee nominatedBy;

    @Column(name = "remarks", length = Integer.MAX_VALUE)
    private String remarks;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "status_code", nullable = false)
    private CertificateStatus statusCode;

    @NotNull
    @Column(name = "actioned_at", nullable = false)
    private Instant actionedAt;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

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