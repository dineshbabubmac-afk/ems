package com.annztech.rewardsystem.modules.auth.service;

import com.annztech.rewardsystem.modules.auth.dto.AuthLoginDTO;
import com.annztech.rewardsystem.modules.auth.dto.AuthResponseDTO;
import com.annztech.rewardsystem.modules.employee.dto.EmployeeDTO;
import org.antlr.v4.runtime.misc.Pair;

public interface AuthService {
    Pair<AuthResponseDTO, String> getAccessToken(AuthLoginDTO authLoginDTO);
    Pair<AuthResponseDTO, String> renewRefreshToken(String refreshToken);
    EmployeeDTO getEmployeeDTO();
}
