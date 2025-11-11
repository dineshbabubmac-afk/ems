package com.annztech.rewardsystem.modules.certificate.certificateemployee.dto;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateResponseDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateEmployeeResponseDTO {
    String id;
    EmployeeDTO employee;
    CertificateResponseDTO certificate;
    String createdAt;
    String updatedAt;

    public CertificateEmployeeResponseDTO(CertificateEmployee certificateEmployee) {
        this.id = certificateEmployee.getId().toString();
        this.employee = new EmployeeDTO(certificateEmployee.getEmployee());
        this.certificate = new CertificateResponseDTO(certificateEmployee.getCertificate());
        this.createdAt = certificateEmployee.getCreatedAt().toString();
        this.updatedAt = certificateEmployee.getUpdatedAt().toString();
    }
}
