package com.annztech.rewardsystem.modules.certificate.template.helper;

import com.annztech.rewardsystem.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class TemplateHelper {
    public static String loadCertificateTemplate(String templatePath, Map<String, String> variables) {
        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            if (!resource.exists()) {
                log.error("Template file not found: {}", templatePath);
                throw new AppException("Template file not found: " + templatePath, HttpStatus.NOT_FOUND);
            }
            byte[] fileBytes = resource.getInputStream().readAllBytes();
            String htmlContent = new String(fileBytes, StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                htmlContent = htmlContent.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
            }
            return htmlContent;
        } catch (IOException e) {
            log.error("Error reading template file {}: {}", templatePath, e.getMessage());
            throw new AppException("Error reading template file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while loading template {}: {}", templatePath, e.getMessage());
            throw new AppException("Unexpected error while loading template: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
