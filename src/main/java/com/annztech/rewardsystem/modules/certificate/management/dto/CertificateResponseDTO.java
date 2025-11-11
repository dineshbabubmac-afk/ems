package com.annztech.rewardsystem.modules.certificate.management.dto;

import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import com.annztech.rewardsystem.modules.certificate.criteria.entity.Criterion;
import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponseDTO {
    private String id;
    private String code;
    private String name;
    private String description;
    private Boolean eligibilityRequired;
    private Boolean approvalRequired;
    private Boolean criteriaRequired;
    private String templateId;
    private Boolean isDeleted;
    private Boolean isActive;
    private Integer totalWeightAge;
    private CertificateCategoryDTO certificateCategory;
    private List<CriteriaDTO> criteria;

    public CertificateResponseDTO(Certificate certificate){
        this.id = certificate.getId().toString();
        this.code = certificate.getCode();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.eligibilityRequired = certificate.getEligibilityRequired();
        this.approvalRequired = certificate.getApprovalRequired();
        this.criteriaRequired = certificate.getCriteriaRequired();
        this.templateId = certificate.getCertificateTemplate().getId().toString();
        this.isDeleted = certificate.getIsDeleted();
        this.totalWeightAge = 100;
        this.isActive = certificate.getIsActive();
        this.certificateCategory = new CertificateCategoryDTO(certificate.getCertificateCategory());
    }

    public CertificateResponseDTO(Certificate certificate, List<Criterion> criteriaList) {
        this(certificate);
        this.criteria = criteriaList.stream().map(CriteriaDTO::new).toList();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CertificateCategoryDTO {
        private String id;
        private String nameEnglish;
        private String nameArabic;

        public CertificateCategoryDTO(CertificateCategory category) {
            this.id = category.getId().toString();
            this.nameEnglish = category.getNameEnglish();
            this.nameArabic = category.getNameArabic();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaDTO {
        private String id;
        private String name;
        private Integer percentage;
        private String description;
        private String certificateId;

        public CriteriaDTO(Criterion criterion) {
            this.id = criterion.getId().toString();
            this.name = criterion.getName();
            this.percentage = criterion.getPercentage();
            this.description = criterion.getDescription();
            this.certificateId = criterion.getCertificate().getId().toString();
        }
    }
}
