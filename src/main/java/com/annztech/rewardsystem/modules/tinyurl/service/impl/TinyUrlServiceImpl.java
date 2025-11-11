package com.annztech.rewardsystem.modules.tinyurl.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import com.annztech.rewardsystem.modules.tinyurl.dto.TinyUrlDTO;
import com.annztech.rewardsystem.modules.tinyurl.entity.TinyUrl;
import com.annztech.rewardsystem.modules.tinyurl.repository.TinyUrlRepository;
import com.annztech.rewardsystem.modules.tinyurl.service.TinyUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Random;

@Service
@Slf4j
public class TinyUrlServiceImpl implements TinyUrlService {

    private final TinyUrlRepository tinyUrlRepository;
    private final TemplateService templateService;
    private final EmployeeService employeeService;

    private static final String ALPHANUM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final Random random = new Random();

    public TinyUrlServiceImpl(TinyUrlRepository tinyUrlRepository, @Lazy TemplateService templateService, @Lazy EmployeeService employeeService) {
        this.tinyUrlRepository = tinyUrlRepository;
        this.templateService = templateService;
        this.employeeService = employeeService;
    }

    @Override
    public String getCertificateFromTinyUrl(String shortCode) {
        var tinyUrl = tinyUrlRepository.findTinyUrlByShortCode(shortCode)
                .orElseThrow(() -> new AppException("Short URL not found", HttpStatus.BAD_REQUEST));
        String longUrl = tinyUrl.getOriginalUrl();
        log.info("Resolved Tiny URL [{}] -> [{}]", shortCode, longUrl);
        String templateId = extractTemplateId(longUrl);
        if (templateId == null)
        {
            throw new AppException("Unable to extract template ID from URL: " + longUrl, HttpStatus.BAD_REQUEST);
        }
        String renderedHtml = templateService.getCertificateTemplateViewById(templateId);
        log.info("Successfully rendered certificate for template [{}]", templateId);
        return renderedHtml;
    }

    @Override
    public String getEmployeeCertificateHtml(String shortCode) {
        var tinyUrl = tinyUrlRepository.findTinyUrlByShortCode(shortCode)
                .orElseThrow(() -> new AppException("Short URL not found", HttpStatus.BAD_REQUEST));
        String longUrl = tinyUrl.getOriginalUrl();
        log.info("Resolved Employee Tiny URL [{}] -> [{}]", shortCode, longUrl);
        URI uri = URI.create(longUrl);
        String[] pathParts = uri.getPath().split("/");
        String templateId = pathParts[pathParts.length - 2];
        String employeeId = uri.getQuery().split("=")[1];
        String renderedHtml = employeeService.getCertificateTemplateViewById(templateId, employeeId);
        log.info("Successfully rendered certificate for template [{}]", templateId);
        return renderedHtml;
    }

    private String extractTemplateId(String longUrl) {
        try {
            URI uri = URI.create(longUrl);
            String[] segments = uri.getPath().split("/");
            return segments[segments.length - 2];
        }
        catch (Exception e) {
            log.error("Failed to extract template ID from URL [{}]: {}", longUrl, e.getMessage());
            return null;
        }
    }

    @Override
    public TinyUrlDTO createTinyUrl(String longUrl)
    {
        String shortCode = generateShortCode();
        TinyUrl tinyUrl = new TinyUrl();
        tinyUrl.setOriginalUrl(longUrl);
        tinyUrl.setShortCode(shortCode);
        tinyUrlRepository.save(tinyUrl);
        String tinyUrlString = buildTinyUrl(shortCode);
        return TinyUrlDTO.builder()
                .longUrl(longUrl)
                .tinyUrl(tinyUrlString)
                .build();
    }

    @Override
    public TinyUrlDTO createEmployeeTinyUrl(String longUrl)
    {
        String shortCode = generateShortCode();
        TinyUrl tinyUrl = new TinyUrl();
        tinyUrl.setOriginalUrl(longUrl);
        tinyUrl.setShortCode(shortCode);
        tinyUrlRepository.save(tinyUrl);
        String tinyUrlString = buildEmployeeTinyUrl(shortCode);
        return TinyUrlDTO.builder()
                .longUrl(longUrl)
                .tinyUrl(tinyUrlString)
                .build();
    }

    private String buildTinyUrl(String shortCode) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/certificate/redirect")
                .queryParam("code", shortCode)
                .toUriString();
    }

    private String buildEmployeeTinyUrl(String shortCode) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/certificate/redirect/me")
                .queryParam("code", shortCode)
                .toUriString();
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
}