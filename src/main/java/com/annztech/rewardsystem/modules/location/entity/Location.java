package com.annztech.rewardsystem.modules.location.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_ar")
    private String nameAr;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
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
