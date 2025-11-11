package com.annztech.rewardsystem.modules.job.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import com.annztech.rewardsystem.modules.job.constants.JobConstant;
import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.job.repository.JobRepository;
import com.annztech.rewardsystem.modules.job.service.JobStateService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Transactional
@Service
public class JobStateServiceImpl extends LocalizationService implements JobStateService {
    private final JobRepository jobRepository;
    private final EmployeeService employeeService;
    public JobStateServiceImpl(JobRepository jobRepository, EmployeeService employeeService) {
        this.jobRepository = jobRepository;
        this.employeeService = employeeService;
    }

    @Override
    public JobDTO activateJob(String jobId) {
        Job job = getJob(jobId);
        job.setIsActive(true);
        job.setIsDeleted(false);
        employeeService.activateEmployeeEntityByJobId(jobId);
        jobRepository.save(job);
        return getJobDTO(job);
    }

    @Override
    public JobDTO deactivateJob(String jobId) {
        Job job = getJob(jobId);
        job.setIsActive(false);
        job.setIsDeleted(true);
        employeeService.deactivateEmployeeEntityByJobId(jobId);
        jobRepository.save(job);
        return getJobDTO(job);
    }

    private Job getJob(String jobId) {
        StringsUtils.validateUUID(jobId, JobConstant.JOB_INVALID_ID);
        UUID jobUuid = UUID.fromString(jobId);
        Job job = jobRepository.findJobById(jobUuid);
        if (job == null) {
            throw new AppException(getMessage(JobConstant.JOB_NO_RECORDS), HttpStatus.NOT_FOUND);
        }
        return job;
    }

    private JobDTO getJobDTO(Job job) {
        return new JobDTO(job);
    }
}
