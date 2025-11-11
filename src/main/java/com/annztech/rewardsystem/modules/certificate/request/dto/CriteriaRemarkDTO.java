package com.annztech.rewardsystem.modules.certificate.request.dto;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequestCriterion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaRemarkDTO {
    String id;
    String remark;
    String criteria;
    String criteriaId;
    String criteriaDescription;
    Integer percentage;
    String fileUrl;

    public CriteriaRemarkDTO(CertificateRequestCriterion criteria){
        this.id = criteria.getId().toString();
        this.remark = criteria.getRemarks();
        this.criteria = criteria.getCriteria().getName();
        this.criteriaDescription = criteria.getCriteria().getDescription();
        this.criteriaId = criteria.getCriteria().getId().toString();
        this.percentage = criteria.getCriteria().getPercentage();
    }
}
