package com.annztech.rewardsystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppError {
    private int code;
    private String message;
}
