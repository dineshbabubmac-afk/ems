package com.annztech.rewardsystem.modules.drives.attachments.mapper;


import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentIdResponseDTO;
import com.annztech.rewardsystem.modules.drives.attachments.dto.FileUploadResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentIdResponseDTO toDTO(String attachmentId);
    FileUploadResponseDTO toDTO(String fileName, String status);
    FileUploadResponseDTO toDTO(String fileName, String status, String attachmentId);
}