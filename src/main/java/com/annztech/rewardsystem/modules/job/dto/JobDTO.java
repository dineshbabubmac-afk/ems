package com.annztech.rewardsystem.modules.job.dto;

import com.annztech.rewardsystem.modules.job.entity.Job;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private String id;
    private String titleEn;
    private String titleAr;
    private String bandLevel;
    private boolean active;
    public JobDTO(Job job) {
        this.id = job.getId().toString();
        this.titleEn = job.getTitleEn();
        this.titleAr = job.getTitleAr();
        this.active = job.getIsActive();
        this.bandLevel = job.getBandLevel().getCode();
    }
}
