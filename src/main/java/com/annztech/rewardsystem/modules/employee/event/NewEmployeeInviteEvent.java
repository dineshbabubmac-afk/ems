package com.annztech.rewardsystem.modules.employee.event;

import com.annztech.rewardsystem.modules.employee.dto.EmployeeInviteDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEmployeeInviteEvent {
    private EmployeeInviteDTO employeeInviteDTO;
}
