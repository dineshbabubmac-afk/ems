package com.annztech.rewardsystem.modules.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private Integer gender;
    private String departmentId;
    private Long locationId;
    private String jobId;
    private String roleId;
    private String specialityId;
}
