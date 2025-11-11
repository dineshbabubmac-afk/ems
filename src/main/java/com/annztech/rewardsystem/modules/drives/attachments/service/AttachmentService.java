package com.annztech.rewardsystem.modules.drives.attachments.service;

import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentRequestDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentUploadResponse;

import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    AttachmentUploadResponse createAttachment(String referenceType, String referenceId, UUID uploadedBy, AttachmentRequestDTO dto);
    List<AttachmentDTO> getAttachment(String referenceType, String id);
}
