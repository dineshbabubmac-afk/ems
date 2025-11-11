package com.annztech.rewardsystem.modules.tinyurl.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TinyUrlDTO {
    private String longUrl;
    private String tinyUrl;
}
