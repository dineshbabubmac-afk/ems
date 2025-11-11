package com.annztech.rewardsystem.modules.drives.attachments.mapper;


import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentDTO;
import com.annztech.rewardsystem.modules.drives.attachments.entity.Attachment;
import org.springframework.stereotype.Component;

@Component
public class AttachmentDTOMapper {
    private final FileUrlMapper fileUrlMapper;

    public AttachmentDTOMapper(FileUrlMapper fileUrlMapper) {
        this.fileUrlMapper = fileUrlMapper;
    }

    public AttachmentDTO toDto(Attachment attachment) {
        return new AttachmentDTO(
                attachment.getId().toString(),
                attachment.getFolder().getId().toString(),
                fileUrlMapper.toPublicUrl(attachment.getFileUrl())
        );
    }
}

