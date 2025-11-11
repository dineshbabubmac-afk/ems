package com.annztech.rewardsystem.modules.lookups.entity;

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
@Table(name = "band_level_lookup")
public class BandLevelLookup {
    @Id
    @Column(name="code", nullable = false)
    private String code;
    @NotNull
    @Column(name = "level", nullable = false)
    private String level;
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