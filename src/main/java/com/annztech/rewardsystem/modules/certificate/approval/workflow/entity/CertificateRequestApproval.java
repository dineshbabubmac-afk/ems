package com.annztech.rewardsystem.modules.certificate.approval.workflow.entity;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "certificate_request_approval")
public class CertificateRequestApproval {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id", nullable = false)
    private Employee member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "certificate_request_id", nullable = false)
    private CertificateRequest certificateRequest;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "status_code", nullable = false)
    private CertificateStatus statusCode;

    @Column(name = "reason", length = Integer.MAX_VALUE)
    private String reason;

    @Column(name = "email_sent")
    private Boolean emailSent = false;

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