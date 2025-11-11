package com.annztech.rewardsystem.modules.drives.folders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FolderDTO {
    private String folderId;
}
