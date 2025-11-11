package com.annztech.rewardsystem.common.service.impl;

import com.annztech.rewardsystem.common.constants.AppConstant;
import com.annztech.rewardsystem.common.service.LocaleMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageServiceImpl implements LocaleMessageService {
    Logger logger = LoggerFactory.getLogger(LocaleMessageServiceImpl.class);
    private final MessageSource messageSource;
    private final RequestContextServiceImpl requestContextServiceImpl;
    public LocaleMessageServiceImpl(MessageSource messageSource,
                                    RequestContextServiceImpl requestContextServiceImpl) {
        this.messageSource = messageSource;
        this.requestContextServiceImpl = requestContextServiceImpl;
    }

    @Override
    public String getMessage(String messageCode) {
        String language = requestContextServiceImpl.getHeader(AppConstant.LANGUAGE);
        if (language == null) {
            language = Locale.getDefault().getLanguage();
        }
        Locale locale = Locale.forLanguageTag(language);
        logger.info("Language: {}", locale.getLanguage());
        return messageSource.getMessage(messageCode, null, messageCode,  locale);
    }
}

