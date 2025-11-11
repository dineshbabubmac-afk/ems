package com.annztech.rewardsystem.common.service;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class LocalizationService {

    @Autowired
    protected LocaleMessageService messageService;

    protected String getMessage(String key) {
        return messageService.getMessage(key);
    }
}

