package com.annztech.rewardsystem.modules.certificate.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestPublishDTO {
    @NotNull(message = "Certificate Id is required")
    private String certificateId;
}
