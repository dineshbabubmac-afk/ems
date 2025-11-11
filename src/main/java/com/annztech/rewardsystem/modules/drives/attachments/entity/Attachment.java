package com.annztech.rewardsystem.modules.drives.attachments.entity;

import com.annztech.rewardsystem.modules.drives.folders.entity.Folder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "size")
    private BigInteger size;

    @Column(name = "content_type", length = Integer.MAX_VALUE)
    private String contentType;

    @NotNull
    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Size(max = 50)
    @NotNull
    @Column(name = "reference_type", nullable = false, length = 50)
    private String referenceType;

    @NotNull
    @Column(name = "uploaded_by", nullable = false)
    private UUID uploadedBy;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @NotNull
    @Column(name = "file_name", nullable = false, length = Integer.MAX_VALUE)
    private String fileName;

    @NotNull
    @Column(name = "file_url", nullable = false, length = Integer.MAX_VALUE)
    private String fileUrl;

    @NotNull
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

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