package com.annztech.rewardsystem.modules.certificate.request.service.impl;

import com.annztech.rewardsystem.modules.certificate.request.repository.CertificateRequestRepository;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CertificateRequestLookUpServiceImpl implements CertificateRequestLookUpService {

    private final CertificateRequestRepository certificateRequestRepository;

    public CertificateRequestLookUpServiceImpl(CertificateRequestRepository certificateRequestRepository) {
        this.certificateRequestRepository = certificateRequestRepository;
    }

    @Override
    public Map<String, Long> getNominationStatusCount(Integer month, Integer year) {
        log.info("Fetching nomination status count for {}/{}", month, year);
        List<Object[]> results = certificateRequestRepository.findNominationStatusCount(month, year);
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }
}
