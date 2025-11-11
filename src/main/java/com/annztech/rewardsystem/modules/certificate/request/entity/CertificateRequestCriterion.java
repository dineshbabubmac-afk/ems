package com.annztech.rewardsystem.modules.certificate.request.entity;

import com.annztech.rewardsystem.modules.certificate.criteria.entity.Criterion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "certificate_request_criteria")
public class CertificateRequestCriterion {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "certificate_request_id", nullable = false)
    private CertificateRequest certificateRequest;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "criteria_id", nullable = false)
    private Criterion criteria;

    @NotNull
    @Column(name = "remarks", nullable = false, length = Integer.MAX_VALUE)
    private String remarks;

    @Column(name = "user_percentage")
    private Integer userPercentage;

    @Column(name = "is_met")
    private Boolean isMet = false;

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