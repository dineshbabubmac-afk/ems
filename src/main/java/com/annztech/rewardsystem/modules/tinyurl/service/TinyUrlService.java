package com.annztech.rewardsystem.modules.tinyurl.service;

import com.annztech.rewardsystem.modules.tinyurl.dto.TinyUrlDTO;

public interface TinyUrlService {
    String getCertificateFromTinyUrl(String code);
    String getEmployeeCertificateHtml(String shortCode);
    TinyUrlDTO createTinyUrl(String longUrl);
    TinyUrlDTO createEmployeeTinyUrl(String viewUrl);
}
