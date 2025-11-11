package com.annztech.rewardsystem.modules.tinyurl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tiny_url")
public class TinyUrl {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @Column(name = "original_url", nullable = false, length = Integer.MAX_VALUE)
    private String originalUrl;

    @Size(max = 20)
    @NotNull
    @Column(name = "short_code", nullable = false, length = 20)
    private String shortCode;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}