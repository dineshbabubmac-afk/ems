package com.annztech.rewardsystem.common.utils;

import com.annztech.rewardsystem.common.exception.AppException;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static final String IMAGE_DIR = "uploads/images/";
    public static final String ATTACHMENT_DIR = "uploads/task/attachment";
    public static final String PROJECTS_ATTACHMENT_DIR = "uploads/projects";

    public static void createImageDirectoryIfNotExist() {
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void createAttachmentDirectoryIfNotExist() {
        File dir = new File(ATTACHMENT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void createDirectoryIfNotExist(Path fullPath)  {
        try {
            // Create folders if they don't exist
            Files.createDirectories(fullPath);
        } catch (IOException e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String generateFileUrl(String basePath, String fullPath) {
        if (!fullPath.startsWith(basePath)) {
            throw new IllegalArgumentException("File path is not under upload directory");
        }

        String relativePath = fullPath.substring(basePath.length());
        String encodedPath = URLEncoder.encode(relativePath, StandardCharsets.UTF_8)
                .replace("+", "%20")
                .replace("%2F", "/");

        // Build base URL from current request
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + encodedPath;
    }

    public static String getFileExtension(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return null;
        }

        String fileName = file.getOriginalFilename();
        int dotIndex = fileName.lastIndexOf('.');

        // Check if the file has an extension
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        } else {
            return ""; // or null if you prefer
        }
    }

    public static boolean acceptableSize(MultipartFile file){
        return file.getSize() < 3 * 1024 * 1024;
    }
}
