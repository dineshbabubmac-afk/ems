package com.annztech.rewardsystem.modules.invite.entity;

import com.annztech.rewardsystem.modules.invite.enums.InviteStatus;
import com.annztech.rewardsystem.modules.lookups.entity.AppTypeLookUp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "invite")
public class Invite {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 254)
    @NotNull
    @Column(name = "receiver_email", nullable = false, length = 254)
    private String receiverEmail;

    @Size(max = 254)
    @NotNull
    @Column(name = "sender_email", nullable = false, length = 254)
    private String senderEmail;

    @Column(name = "status")
    private String status = InviteStatus.PENDING.toString();

    @Column(name = "reference_id")
    private UUID referenceId;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @ColumnDefault("true")
    @Column(name = "resend_count")
    private Integer resendCount;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @NotNull
    @Column(name = "app_type")
    private String appType;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (receiverEmail != null) receiverEmail = receiverEmail.toLowerCase();
        if (senderEmail != null) senderEmail = senderEmail.toLowerCase();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        if (receiverEmail != null) receiverEmail = receiverEmail.toLowerCase();
        if (senderEmail != null) senderEmail = senderEmail.toLowerCase();
    }
}