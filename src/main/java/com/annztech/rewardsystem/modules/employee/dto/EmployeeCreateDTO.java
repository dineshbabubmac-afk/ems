package com.annztech.rewardsystem.modules.employee.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreateDTO {
    @NotBlank(message = "{employee.first.name.required}")
    private String firstName;
    @NotBlank(message = "{employee.last.name.required}")
    private String lastName;
    @NotBlank(message = "{employee.email.required}")
    private String email;
    @NotBlank(message = "{employee.mobile.required}")
    private String mobileNumber;
    @NotNull(message = "{employee.gender.required}")
    @Min(value = 0, message = "{gender.invalidCode}")
    @Max(value = 1, message = "{gender.invalidCode}")
    private Integer gender;
    @NotNull(message = "{employee.department.required}")
    private String departmentId;
    @NotNull(message = "{employee.location.required}")
    private Long LocationId;
    @NotBlank(message = "{employee.job.required}")
    private String jobId;
}
