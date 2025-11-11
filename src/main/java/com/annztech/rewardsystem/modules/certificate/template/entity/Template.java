package com.annztech.rewardsystem.modules.certificate.template.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "certificate_template")
public class Template {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;
    @NotNull
    @Column(name = "path", nullable = false)
    private String path;
    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;
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