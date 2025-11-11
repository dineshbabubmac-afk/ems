package com.annztech.rewardsystem.modules.job.service.impl;

import com.annztech.rewardsystem.modules.job.repository.JobRepository;
import com.annztech.rewardsystem.modules.job.service.JobLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobLookUpServiceImpl implements JobLookUpService {

    private final JobRepository jobRepository;

    public JobLookUpServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Long getJobsCount(Integer month, Integer year) {
        return jobRepository.countJobsByMonthYear(month, year);
    }
}
