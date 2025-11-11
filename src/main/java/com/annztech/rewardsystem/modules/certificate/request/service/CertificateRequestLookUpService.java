package com.annztech.rewardsystem.modules.certificate.request.service;

import java.util.Map;

public interface CertificateRequestLookUpService {

    Map<String, Long> getNominationStatusCount(Integer month, Integer year);
}
