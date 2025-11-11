package com.annztech.rewardsystem.modules.certificate.request.service.impl;

import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.entity.CertificateRequestApproval;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.repository.CertificateRequestApprovalRepository;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestAssignmentService;
import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CertificateRequestAssignmentImpl extends LocalizationService implements CertificateRequestAssignmentService {
    private final CertificateRequestApprovalRepository certificateRequestApprovalRepository;

    public CertificateRequestAssignmentImpl(CertificateRequestApprovalRepository certificateRequestApprovalRepository) {
        this.certificateRequestApprovalRepository = certificateRequestApprovalRepository;
    }

    @Override
    public void certificateRequestAssignedToCommitteeMembers(List<Employee> allParticularCommitteeMembers, CertificateRequest certificateRequest, CertificateStatus certificateStatus) {
        List<CertificateRequestApproval> approvals = new ArrayList<>();
        for (Employee member : allParticularCommitteeMembers) {
            CertificateRequestApproval approval = new CertificateRequestApproval();
            approval.setCertificateRequest(certificateRequest);
            approval.setMember(member);
            approval.setStatusCode(certificateStatus);
            approvals.add(approval);
        }
        certificateRequestApprovalRepository.saveAll(approvals);
    }
}
