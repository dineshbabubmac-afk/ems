package com.annztech.rewardsystem.modules.job.service;

import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import org.springframework.stereotype.Service;

@Service
public interface JobStateService {
    JobDTO activateJob(String jobId);
    JobDTO deactivateJob(String jobId);
}
