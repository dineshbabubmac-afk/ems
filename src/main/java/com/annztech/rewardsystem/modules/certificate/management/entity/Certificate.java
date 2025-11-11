package com.annztech.rewardsystem.modules.certificate.management.entity;

import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "certificate_category_id", nullable = false)
    private CertificateCategory certificateCategory;

    @Size(max = 20)
    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Size(max = 254)
    @NotNull
    @Column(name = "name", nullable = false, length = 254)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "eligibility_required", nullable = false)
    private Boolean eligibilityRequired = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "criteria_required", nullable = false)
    private Boolean criteriaRequired = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "certificate_template_id", nullable = false)
    private Template certificateTemplate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

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