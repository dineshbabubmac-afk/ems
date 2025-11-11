package com.annztech.rewardsystem.modules.drives.attachments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResponseDTO {
    private String fileName;
    private String status;
    private String attachmentId;
    private String error;
}