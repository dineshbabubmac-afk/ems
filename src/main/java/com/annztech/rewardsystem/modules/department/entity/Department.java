package com.annztech.rewardsystem.modules.department.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "name_en", nullable = false)
    private String nameEn;
    @Column(name = "name_ar", nullable = false)
    private String nameAr;
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
