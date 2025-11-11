package com.annztech.rewardsystem.modules.drives.service.impl;

import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentRequestDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentUploadResponse;
import com.annztech.rewardsystem.modules.drives.attachments.service.AttachmentService;
import com.annztech.rewardsystem.modules.drives.dto.DriveDTO;
import com.annztech.rewardsystem.modules.drives.folders.dto.FolderDTO;
import com.annztech.rewardsystem.modules.drives.folders.service.FolderService;
import com.annztech.rewardsystem.modules.drives.service.DriveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DriveServiceImpl implements DriveService {
    private final FolderService folderService;
    private final AttachmentService attachmentService;
    public DriveServiceImpl(FolderService folderService, AttachmentService attachmentService) {
        this.folderService = folderService;
        this.attachmentService = attachmentService;
    }

    @Override
    public Boolean createDriveAndUpload(DriveDTO dto) {
        AttachmentRequestDTO requestDTO = new AttachmentRequestDTO();
        requestDTO.setFileMap(dto.getFiles());
        requestDTO.setFolderId(dto.getFolderId());
        AttachmentUploadResponse attachmentUploadResponse = attachmentService.createAttachment(dto.getReferencedType(), dto.getReferencedId(), dto.getUploadedBy(), requestDTO);
        return attachmentUploadResponse != null;
    }

    @Override
    public List<AttachmentDTO> getAttachment(String referenceType, String id) {
       return attachmentService.getAttachment(referenceType, id);
    }

    @Override
    public String getFolderId(String referenceType, String id) {
        FolderDTO folderDTO = folderService.getFolder(referenceType, id);
        return folderDTO.getFolderId();
    }
}
