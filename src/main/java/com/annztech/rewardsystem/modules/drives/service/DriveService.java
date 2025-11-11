package com.annztech.rewardsystem.modules.drives.service;

import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentDTO;
import com.annztech.rewardsystem.modules.drives.dto.DriveDTO;

import java.util.List;

public interface DriveService {
    Boolean createDriveAndUpload(DriveDTO dto);
    List<AttachmentDTO> getAttachment(String referenceType, String id);
    String getFolderId(String referenceType, String id);
}
