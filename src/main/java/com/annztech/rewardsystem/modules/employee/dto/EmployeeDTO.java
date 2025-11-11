package com.annztech.rewardsystem.modules.employee.dto;

import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import com.annztech.rewardsystem.modules.location.dto.LocationDTO;
import com.annztech.rewardsystem.modules.role.dto.RoleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {
    private String id;
    private String code;
    private String firstName;
    private Instant lastLogin;
    private String lastName;
    private String fullName;
    private String email;
    private String mobileNumber;
    private Integer gender;
    private String profilePicture;
    private DepartmentDTO department;
    private LocationDTO location;
    private JobDTO job;
    private RoleDTO role;
    private String roleName;
    private String departmentName;
    private Boolean active;
    private String certificateCommitteeMemberId;


    public EmployeeDTO(Employee employee) {
        this.id = employee.getId().toString();
        this.code = employee.getEmployeeCode();
        this.lastLogin = employee.getLastLoginAt();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.fullName = StringsUtils.capitalizeName(employee.getFirstName()) + " " + StringsUtils.capitalizeName(employee.getLastName());
        this.email = employee.getEmail();
        this.mobileNumber = employee.getMobileNumber();
        this.gender = employee.getGender();
        this.department = new DepartmentDTO(employee.getDepartment());
        this.job = new JobDTO(employee.getJob());
        this.location = new LocationDTO(employee.getLocation());
        this.role = new RoleDTO(employee.getRole());
        this.profilePicture = employee.getProfilePicturePath();
        this.active = employee.getIsActive() != null && employee.getIsActive();
    }



    public EmployeeDTO(Employee employee, boolean simplified) {
        this.id = employee.getId().toString();
        this.code = employee.getEmployeeCode();
        this.lastLogin = employee.getLastLoginAt();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.fullName = StringsUtils.capitalizeName(employee.getFirstName()) + " " + StringsUtils.capitalizeName(employee.getLastName());
        this.email = employee.getEmail();
        this.mobileNumber = employee.getMobileNumber();
        this.gender = employee.getGender();
        this.profilePicture = employee.getProfilePicturePath();
        this.active = employee.getIsActive() != null && employee.getIsActive();
        this.department = new DepartmentDTO(employee.getDepartment());
    }
    public EmployeeDTO(String id, String firstName, String lastName, String email, Department department){
        this.id = id;
        this.fullName = StringsUtils.capitalizeName(firstName) + " " + StringsUtils.capitalizeName(lastName);
        this.email = email;
        this.department = new DepartmentDTO(department);
    }

    public EmployeeDTO(Employee employee, String certificateMemberId){
        this(employee);
        this.certificateCommitteeMemberId = certificateMemberId;
    }
}
