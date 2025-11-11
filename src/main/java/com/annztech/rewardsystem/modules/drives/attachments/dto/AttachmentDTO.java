package com.annztech.rewardsystem.modules.drives.attachments.dto;

import com.annztech.rewardsystem.modules.drives.attachments.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private String id;
    private String folderId;
    private String fileUrl;

    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId().toString();
        this.fileUrl = attachment.getFileUrl();
        this.folderId = attachment.getFolder().getId().toString();
    }

}

