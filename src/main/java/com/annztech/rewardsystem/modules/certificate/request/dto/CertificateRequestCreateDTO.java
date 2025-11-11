package com.annztech.rewardsystem.modules.certificate.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestCreateDTO {
    @NotNull(message = "To Nominated Employee Id is required")
    private String nominatedToEmployee;
    @NotNull(message = "Certificate Id is Required")
    private String certificateId;
    private String criteriaJSONString;
    private List<MultipartFile> files;
}
