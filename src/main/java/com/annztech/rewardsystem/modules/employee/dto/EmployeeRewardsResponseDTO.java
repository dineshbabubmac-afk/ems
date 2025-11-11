package com.annztech.rewardsystem.modules.employee.dto;

import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRewardsResponseDTO {
    private String name;
    private String description;
    private String date;
    private String category;
    private String empCode;
    private String certificateName;
    private String templateURL;

    public EmployeeRewardsResponseDTO(Employee employee, CertificateEmployee ce) {
        this.name = employee.getFirstName();
        this.description = ce.getCertificate().getDescription();
        this.date = ce.getCertificate().getCreatedAt().toString();
        this.category = ce.getCertificate().getCertificateCategory().getNameEnglish();
        this.empCode = employee.getEmployeeCode();
        this.certificateName = ce.getCertificate().getName();
    }
}
