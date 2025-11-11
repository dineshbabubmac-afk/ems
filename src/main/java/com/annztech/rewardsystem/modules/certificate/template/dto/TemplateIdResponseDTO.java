package com.annztech.rewardsystem.modules.certificate.template.dto;

import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateIdResponseDTO {
    private String id;

    public TemplateIdResponseDTO(Template template) {
        this.id = template.getId().toString();
    }
}
