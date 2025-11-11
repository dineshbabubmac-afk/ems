package com.annztech.rewardsystem.modules.certificate.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestUpdateDTO {
    private String nominatedToEmployee;
    private String nominatedByEmployee;
    private String certificateId;
    private String remark;
    private String statusCode;
}
