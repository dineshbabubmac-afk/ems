package com.annztech.rewardsystem.modules.job.service;

import com.annztech.rewardsystem.modules.job.dto.JobCreateDTO;
import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import com.annztech.rewardsystem.modules.job.dto.JobUpdateDTO;
import com.annztech.rewardsystem.modules.job.entity.Job;

import java.util.List;

public interface JobService {
    JobDTO createJobDTO(JobCreateDTO job);
    JobDTO updateJobDTO(String id, JobUpdateDTO job);
    JobDTO deleteJobDTO(String id);
    JobDTO getJobDTOById(String id);
    List<JobDTO> getAllJobDTO();

    //For composite service communication
    Job getJobEntityById(String id);
    Job getJobEntityByTitleEn(String name);
    List<JobDTO>  searchJobDTO(String query);

}
