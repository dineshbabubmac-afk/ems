package com.annztech.rewardsystem.modules.employee.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePicResponseDTO {
    String id;
    String employeeCode;
    String profilePictureURL;

    public EmployeePicResponseDTO(String id, String code, String url) {
        this.id = id;
        this.employeeCode = code;
        this.profilePictureURL = url;
    }
}