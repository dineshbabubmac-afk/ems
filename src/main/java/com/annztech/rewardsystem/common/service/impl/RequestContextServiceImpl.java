package com.annztech.rewardsystem.common.service.impl;

import com.annztech.rewardsystem.common.service.RequestContextService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope //each request is created - multi thread scenarios
public class RequestContextServiceImpl implements RequestContextService {

    private final HttpServletRequest httpServletRequest;
    public RequestContextServiceImpl(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public String getHeader(String key) {
        return httpServletRequest.getHeader(key);
    }
}

