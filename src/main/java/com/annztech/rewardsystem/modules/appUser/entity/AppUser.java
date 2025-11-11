package com.annztech.rewardsystem.modules.appUser.entity;

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
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Size(max = 254)
    @NotNull
    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Size(max = 20)
    @Column(name = "user_code", length = 20)
    private String userCode;

    @Size(max = 20)
    @NotNull
    @Column(name = "app_code", nullable = false, length = 20)
    private String appCode;

    @NotNull
    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Size(max = 20)
    @ColumnDefault("'PENDING'")
    @Column(name = "status", length = 20)
    private String status;

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