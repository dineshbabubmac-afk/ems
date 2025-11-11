package com.annztech.rewardsystem.modules.drives.attachments.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUrlMapper {

    @Value("${app.file-url}")
    private String fileUrl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String toPublicUrl(String relativePath) {
        String path = relativePath.replace(uploadDir, "");
        return fileUrl + "/files" + path;
    }
}
