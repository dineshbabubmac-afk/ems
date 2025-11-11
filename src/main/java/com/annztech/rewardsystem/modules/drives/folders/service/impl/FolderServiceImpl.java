package com.annztech.rewardsystem.modules.drives.folders.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.drives.folders.entity.Folder;
import com.annztech.rewardsystem.modules.drives.attachments.repository.FolderRepository;
import com.annztech.rewardsystem.modules.drives.config.FileResourceConfig;
import com.annztech.rewardsystem.modules.drives.folders.dto.FolderDTO;
import com.annztech.rewardsystem.modules.drives.folders.helper.FolderHelper;
import com.annztech.rewardsystem.modules.drives.folders.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FolderServiceImpl extends LocalizationService implements FolderService {

    private final FileResourceConfig resourceConfig;
    private final FolderRepository folderRepository;

    public FolderServiceImpl(FileResourceConfig resourceConfig, FolderRepository folderRepository) {
        this.resourceConfig = resourceConfig;
        this.folderRepository = folderRepository;
    }

    @Override
    public FolderDTO getFolder(String code, String referenceId) {
        StringsUtils.validateUUID(referenceId, getMessage("Invalid reference id"));
        UUID refUUID = UUID.fromString(referenceId);
        return folderRepository.findFolderByReferenceId(refUUID)
                .map(folder -> FolderDTO.builder().folderId(folder.getId().toString()).build())
                .orElseGet(() -> createAndSaveFolder(code, referenceId, refUUID));
    }

    @Override
    public Folder getFolderEntityById(String id) {
        StringsUtils.validateUUID(id, "Invalid folder ID:" + id);
        UUID uuid = UUID.fromString(id);
        return folderRepository.findFolderById(uuid).orElseThrow(() -> new AppException("Folder not found", HttpStatus.NOT_FOUND));
    }

    private FolderDTO createAndSaveFolder(String code, String referenceId, UUID referenceUUID) {
        String path = FolderHelper.createNewCertificateNominationCriteriaDirectory(code, referenceId, resourceConfig.uploadDir);
        Folder folder = new Folder();
        folder.setReferenceId(referenceUUID);
        folder.setReferenceType(code);
        folder.setPath(path);
        Folder saved = folderRepository.save(folder);
        return FolderDTO.builder().folderId(saved.getId().toString()).build();
    }
}
