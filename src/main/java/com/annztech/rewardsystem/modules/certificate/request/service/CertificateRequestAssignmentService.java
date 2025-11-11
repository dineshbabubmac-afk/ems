package com.annztech.rewardsystem.modules.certificate.request.service;

import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.employee.entity.Employee;

import java.util.List;

public interface CertificateRequestAssignmentService {
    void certificateRequestAssignedToCommitteeMembers(List<Employee> allParticularCommitteeMembers, CertificateRequest certificateRequest, CertificateStatus certificateStatus);
}
