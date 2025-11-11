package com.annztech.rewardsystem.modules.drives.attachments.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.utils.FileUtils;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentRequestDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentUploadResponse;
import com.annztech.rewardsystem.modules.drives.attachments.dto.FileUploadResponseDTO;
import com.annztech.rewardsystem.modules.drives.attachments.entity.Attachment;
import com.annztech.rewardsystem.modules.drives.attachments.mapper.AttachmentDTOMapper;
import com.annztech.rewardsystem.modules.drives.attachments.mapper.AttachmentMapper;
import com.annztech.rewardsystem.modules.drives.attachments.service.AttachmentService;
import com.annztech.rewardsystem.modules.drives.folders.entity.Folder;
import com.annztech.rewardsystem.modules.drives.folders.repository.AttachmentRepository;
import com.annztech.rewardsystem.modules.drives.folders.service.FolderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final AttachmentDTOMapper attachmentDTOMapper;
    private final FolderService folderService;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, AttachmentDTOMapper attachmentDTOMapper, FolderService folderService) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.attachmentDTOMapper = attachmentDTOMapper;
        this.folderService = folderService;
    }

    @Override
    @Transactional
    public AttachmentUploadResponse createAttachment(String referenceType, String referenceId, UUID uploadedBy, AttachmentRequestDTO dto) {
        List<FileUploadResponseDTO> responseList = new ArrayList<>();
        Folder folder = folderService.getFolderEntityById(dto.getFolderId());
        dto.getFileMap().forEach((id, file) ->  {
            if (!FileUtils.acceptableSize(file)) {
                responseList.add(attachmentMapper.toDTO(file.getOriginalFilename(), "failed to upload because it exceeds the 3 MB size limit."));
            }
            Path filePath = null;
            String fileName = id + "." + FileUtils.getFileExtension(file);
            try {
                UUID referenceIdUuid = UUID.fromString(referenceId);
                filePath = Paths.get(folder.getPath(), fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                Attachment attachment = new Attachment();
                attachment.setUploadedAt(Instant.now());
                attachment.setUploadedBy(uploadedBy);
                attachment.setReferenceId(referenceIdUuid);
                attachment.setReferenceType(referenceType.toUpperCase());
                attachment.setFolder(folder);
                attachment.setContentType(file.getContentType());
                attachment.setFileUrl(filePath.toString());
                attachment.setFileName(fileName);
                attachment.setSize(BigInteger.valueOf(file.getSize()));
                attachmentRepository.save(attachment);
                responseList.add(attachmentMapper.toDTO(fileName, " is success", attachment.getId().toString()));
            } catch (Exception e) {
                if (filePath != null) {
                    throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
                responseList.add(attachmentMapper.toDTO(file.getOriginalFilename(), "failed"));
            }
        });
        return AttachmentUploadResponse.builder().files(responseList).build();
    }

    @Override
    public List<AttachmentDTO> getAttachment(String referenceType, String referenceId) {
        StringsUtils.validateUUID(referenceId, "Invalid reference id");
        return attachmentRepository.findAttachmentsByReferenceId(UUID.fromString(referenceId)).stream().map((attachmentDTOMapper::toDto)).collect(Collectors.toList());
    }
}
