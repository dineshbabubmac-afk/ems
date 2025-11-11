package com.annztech.rewardsystem.modules.drives.attachments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentRequestDTO {

    @NotBlank(message = "{attachment.folderId}")
    private String folderId;
    private Map<String, MultipartFile> fileMap;
}
