package com.annztech.rewardsystem.modules.drives.attachments.repository;

import com.annztech.rewardsystem.modules.drives.folders.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, UUID> {
    Optional<Folder> findFolderByReferenceId(UUID referenceId);
    Optional<Folder> findFolderById(UUID id);
}
