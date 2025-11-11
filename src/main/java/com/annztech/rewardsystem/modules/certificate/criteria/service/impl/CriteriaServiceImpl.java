package com.annztech.rewardsystem.modules.certificate.criteria.service.impl;

import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.modules.certificate.criteria.repository.CriteriaRepository;
import com.annztech.rewardsystem.modules.certificate.criteria.service.CriteriaService;
import org.springframework.stereotype.Service;

@Service
public class CriteriaServiceImpl implements CriteriaService {
    private final CriteriaRepository criteriaRepository;
    private final SequenceGeneratorRepository sequenceGenerator;
    public CriteriaServiceImpl(CriteriaRepository criteriaRepository, SequenceGeneratorRepository sequenceGenerator) {
        this.criteriaRepository = criteriaRepository;
        this.sequenceGenerator = sequenceGenerator;
    }
}
