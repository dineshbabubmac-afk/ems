package com.annztech.rewardsystem.modules.drives.attachments.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttachmentIdResponseDTO {
    private String uuid;
    public AttachmentIdResponseDTO(UUID uuid) {
        this.uuid = uuid.toString();
    }
}
