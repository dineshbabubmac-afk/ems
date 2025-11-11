package com.annztech.rewardsystem.modules.job.entity;

import com.annztech.rewardsystem.modules.lookups.entity.BandLevelLookup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "title_en", nullable = false)
    private String titleEn;
    @Column(name = "title_ar", nullable = false)
    private String titleAr;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    @Column(name = "is_active")
    private Boolean isActive = true;

    @JoinColumn(name = "band_level_lookup_code", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private BandLevelLookup bandLevel;

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
