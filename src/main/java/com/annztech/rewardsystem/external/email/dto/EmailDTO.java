package com.annztech.rewardsystem.external.email.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDTO {
    public String toEmail;
    public String subject;
}
