package com.annztech.rewardsystem.external.email.service;

public interface EmailService {
    void sendEmail(String toAddress, String subject, String body);
}
