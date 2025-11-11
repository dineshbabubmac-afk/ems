package com.annztech.rewardsystem.modules.certificate.template.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateCreateDTO {
    private String name;
    private String path;
    private String description;
}
