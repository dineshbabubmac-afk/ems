package com.annztech.rewardsystem.external.email.service;

public interface EmailProviderClient {
    void sendEmail(String fromAddress, String toAddress, String subject, String body);
}