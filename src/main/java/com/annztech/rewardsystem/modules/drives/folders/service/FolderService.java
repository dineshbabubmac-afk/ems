package com.annztech.rewardsystem.modules.drives.folders.service;

import com.annztech.rewardsystem.modules.drives.folders.entity.Folder;
import com.annztech.rewardsystem.modules.drives.folders.dto.FolderDTO;

public interface FolderService {
    FolderDTO getFolder(String code, String referenceId);
    Folder getFolderEntityById(String id);
}

