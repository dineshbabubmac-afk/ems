package com.annztech.rewardsystem.modules.drives.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DriveDTO {
    private String referencedId;
    private String referencedType;
    private String folderId;
    private UUID uploadedBy;
    private Map<String, MultipartFile> files;
}
