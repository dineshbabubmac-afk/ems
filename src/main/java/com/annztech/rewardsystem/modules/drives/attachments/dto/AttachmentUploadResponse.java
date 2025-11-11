package com.annztech.rewardsystem.modules.drives.attachments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentUploadResponse {
    private List<FileUploadResponseDTO> files;
    private String step;
}
