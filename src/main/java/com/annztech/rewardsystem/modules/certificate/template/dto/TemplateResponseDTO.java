package com.annztech.rewardsystem.modules.certificate.template.dto;

import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponseDTO {
    private String id;
    private String name;
    private String code;
    private String path;
    private String description;
    private Boolean isActive;
    private String templateContent;

    public TemplateResponseDTO(Template template){
        this.id = template.getId().toString();
        this.name = template.getName();
        this.code = template.getCode();
        this.path = template.getPath();
        this.description = template.getDescription();
        this.isActive = template.getIsActive();
    }
}
