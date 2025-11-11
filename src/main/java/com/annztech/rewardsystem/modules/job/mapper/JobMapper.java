package com.annztech.rewardsystem.modules.job.mapper;

import com.annztech.rewardsystem.modules.job.dto.JobCreateDTO;
import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import com.annztech.rewardsystem.modules.job.dto.JobUpdateDTO;
import com.annztech.rewardsystem.modules.job.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bandLevel", ignore = true)
    Job toEntity(JobCreateDTO jobCreateDTO);
    @Mapping(target = "bandLevel", ignore = true)
    JobDTO toDto(Job job);
}
