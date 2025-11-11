package com.annztech.rewardsystem.modules.job.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.ValidationUtils;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.job.constants.JobConstant;
import com.annztech.rewardsystem.modules.job.dto.JobCreateDTO;
import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import com.annztech.rewardsystem.modules.job.dto.JobUpdateDTO;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.job.mapper.JobMapper;
import com.annztech.rewardsystem.modules.job.repository.JobRepository;
import com.annztech.rewardsystem.modules.job.service.JobService;
import com.annztech.rewardsystem.modules.lookups.entity.BandLevelLookup;
import com.annztech.rewardsystem.modules.lookups.service.BandLevelLookupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobServiceImpl extends LocalizationService implements JobService {
    private final BandLevelLookupService bandLevelLookupService;
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    public JobServiceImpl(BandLevelLookupService bandLevelLookupService, JobRepository jobRepository, JobMapper jobMapper) {
        this.bandLevelLookupService = bandLevelLookupService;
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    //JOB MANAGEMENT FROM EMPLOYEE ADMIN
    @Override
    public JobDTO createJobDTO(JobCreateDTO job) {
        BandLevelLookup bandLevelLookup = bandLevelLookupService.getEntityByBandCode(job.getBandLevel());
        Job jobEntity = jobMapper.toEntity(job);
        jobEntity.setBandLevel(bandLevelLookup);
        Job savedJob = jobRepository.save(jobEntity);
        return jobMapper.toDto(savedJob);
    }

    @Override
    public JobDTO updateJobDTO(String id, JobUpdateDTO jobDto) {
        Job jobEntity = validateAndGetJobEntity(id);
        if (!StringUtils.isNotBlank(jobDto.getTitleAr()) && !StringUtils.isNotBlank(jobDto.getTitleEn())) {
            throw new AppException(getMessage(JobConstant.JOB_NAME_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        if(!StringUtils.isNotBlank(jobDto.getBandLevel())){
            BandLevelLookup bandLevelLookup = bandLevelLookupService.getEntityByBandCode(jobDto.getBandLevel());
            jobEntity.setBandLevel(bandLevelLookup);
        }
        if (StringUtils.isNotBlank(jobDto.getTitleAr())) {
            jobEntity.setTitleAr((jobDto.getTitleAr()));
        }
        if (StringUtils.isNotBlank(jobDto.getTitleEn())) {
            jobEntity.setTitleEn((jobDto.getTitleEn()));
        }
        return new JobDTO(jobRepository.save(jobEntity));
    }

    @Override
    public JobDTO deleteJobDTO(String id) {
        Job job = validateAndGetJobEntity(id);
        job.setIsActive(false);
        job.setIsDeleted(true);
        jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    @Override
    public JobDTO getJobDTOById(String id) {
        Job jobEntity = validateAndGetJobEntity(id);
        return jobMapper.toDto(jobEntity);
    }

    @Override
    public List<JobDTO> getAllJobDTO() {
        List<Job> jobList = jobRepository.findAll(DomainHelper.sortByUpdatedAtDesc());
        return jobList.stream().map(JobDTO::new).toList();
    }

    @Override
    public Job getJobEntityById(String id) {
        return validateAndGetJobEntity(id);
    }

    @Override
    public Job getJobEntityByTitleEn(String name) {
        if (StringUtils.isBlank(name)) {
            throw new AppException(getMessage(JobConstant.JOB_ID_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        return jobRepository.findJobByTitleEn(name).orElseThrow(() -> new AppException(getMessage(JobConstant.JOB_NO_RECORDS), HttpStatus.NOT_FOUND));
    }

    @Override
    public List<JobDTO> searchJobDTO(String query) {
        if (StringUtils.isBlank(query)) {
            throw new AppException(getMessage(JobConstant.CLIENT_JOB_SEARCH), HttpStatus.BAD_REQUEST);
        }
        List<Job> departments = jobRepository.searchByKeyword(query);
        return departments.stream().map(JobDTO::new).toList();
    }

    private Job validateAndGetJobEntity(String id) {
        if (StringUtils.isBlank(id)) {
            throw new AppException(getMessage(JobConstant.JOB_ID_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        if (!ValidationUtils.isValidUUID(id)) {
            throw new AppException(getMessage(JobConstant.JOB_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        return jobRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(JobConstant.JOB_NO_RECORDS), HttpStatus.NOT_FOUND));
    }
}
