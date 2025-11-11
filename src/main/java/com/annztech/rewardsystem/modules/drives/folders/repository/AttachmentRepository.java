package com.annztech.rewardsystem.modules.drives.folders.repository;

import com.annztech.rewardsystem.modules.drives.attachments.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    List<Attachment> findAttachmentsByReferenceId(UUID uuid);
}
