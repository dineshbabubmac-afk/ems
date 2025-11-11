package com.annztech.rewardsystem.modules.certificate.status.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "certificate_status")
public class CertificateStatus {
    @Id
    @Size(max = 10)
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Size(max = 100)
    @NotNull
    @Column(name = "name_english", nullable = false, length = 100)
    private String nameEnglish;

    @Size(max = 100)
    @NotNull
    @Column(name = "name_arabic", nullable = false, length = 100)
    private String nameArabic;

}