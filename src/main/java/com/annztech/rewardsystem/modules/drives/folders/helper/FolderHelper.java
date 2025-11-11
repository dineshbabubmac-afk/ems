package com.annztech.rewardsystem.modules.drives.folders.helper;

import com.annztech.rewardsystem.common.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderHelper {

    public static String createNewCertificateNominationCriteriaDirectory(String code, String ref, String uploadDir) {
        Path path = getDirectory(code, ref, uploadDir );
        FileUtils.createDirectoryIfNotExist(path);
        return path.toString();
    }

    public static Path getDirectory(String code, String dirPath) {
        return Paths.get(dirPath, code);
    }

    public static Path getDirectory(String code, String ref, String dirPath) {
        return Paths.get(dirPath, code, ref);
    }


}
