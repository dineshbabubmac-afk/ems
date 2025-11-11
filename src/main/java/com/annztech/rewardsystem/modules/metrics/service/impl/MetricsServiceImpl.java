package com.annztech.rewardsystem.modules.metrics.service.impl;

import com.annztech.rewardsystem.modules.certificate.approval.assignment.service.CertificateCommitteeLookUpService;
import com.annztech.rewardsystem.modules.certificate.category.service.CertificateCategoryLookUpService;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.service.CertificateEmployeeLookUpService;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestLookUpService;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateLookUpService;
import com.annztech.rewardsystem.modules.department.service.DepartmentLookUpService;
import com.annztech.rewardsystem.modules.job.service.JobLookUpService;
import com.annztech.rewardsystem.modules.location.service.LocationLookUpService;
import com.annztech.rewardsystem.modules.lookups.service.CertificateLookUpService;
import com.annztech.rewardsystem.modules.lookups.service.EmployeeLookUpService;
import com.annztech.rewardsystem.modules.metrics.dto.AdminMetricsResponseDTO;
import com.annztech.rewardsystem.modules.metrics.dto.EmployeeMetricsResponseDTO;
import com.annztech.rewardsystem.modules.metrics.dto.TopEmployeesDTO;
import com.annztech.rewardsystem.modules.metrics.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MetricsServiceImpl implements MetricsService {

    private final CertificateRequestLookUpService certificateRequestLookUpService;
    private final EmployeeLookUpService employeeLookUpService;
    private final CertificateLookUpService certificateLookUpService;
    private final CertificateEmployeeLookUpService certificateEmployeeLookUpService;
    private final DepartmentLookUpService departmentLookUpService;
    private final CertificateCategoryLookUpService certificateCategoryLookUpService;
    private final TemplateLookUpService templateLookUpService;
    private final LocationLookUpService locationLookUpService;
    private final JobLookUpService jobLookUpService;
    private final CertificateCommitteeLookUpService certificateCommitteeLookUpService;

    public MetricsServiceImpl(CertificateRequestLookUpService certificateRequestLookUpService, EmployeeLookUpService employeeLookUpService, CertificateLookUpService certificateLookUpService, CertificateEmployeeLookUpService certificateEmployeeLookUpService, DepartmentLookUpService departmentLookUpService, CertificateCategoryLookUpService certificateCategoryLookUpService, TemplateLookUpService templateLookUpService, LocationLookUpService locationLookUpService, JobLookUpService jobLookUpService, CertificateCommitteeLookUpService certificateCommitteeLookUpService) {
        this.certificateRequestLookUpService = certificateRequestLookUpService;
        this.employeeLookUpService = employeeLookUpService;
        this.certificateLookUpService = certificateLookUpService;
        this.certificateEmployeeLookUpService = certificateEmployeeLookUpService;
        this.departmentLookUpService = departmentLookUpService;
        this.certificateCategoryLookUpService = certificateCategoryLookUpService;
        this.templateLookUpService = templateLookUpService;
        this.locationLookUpService = locationLookUpService;
        this.jobLookUpService = jobLookUpService;
        this.certificateCommitteeLookUpService = certificateCommitteeLookUpService;
    }


    @Override
    public AdminMetricsResponseDTO getAdminOverAllStatistics(Integer month, Integer year) {
        log.info("Building Admin Overall Metrics for {}/{}", month, year);
        AdminMetricsResponseDTO response = new AdminMetricsResponseDTO();

        Map<String, Long> statusCounts = certificateRequestLookUpService.getNominationStatusCount(month, year);
        AdminMetricsResponseDTO.NominationStatusDTO nominationStatus = new AdminMetricsResponseDTO.NominationStatusDTO(
                statusCounts.values().stream().mapToLong(Long::longValue).sum(),
                statusCounts.getOrDefault("PENDING", 0L),
                statusCounts.getOrDefault("APPROVED", 0L),
                statusCounts.getOrDefault("REJECTED", 0L),
                statusCounts.getOrDefault("PUBLISHED", 0L));
        response.setNominationStatus(nominationStatus);

        Map<String, Long> categoryWise = certificateLookUpService.getCertificatesCategoryWise(month, year);
        response.setCertificatesCategoryWise(categoryWise);

        Map<String, Long> departmentOverview = employeeLookUpService.getDepartmentOverview(month, year);
        response.setDepartmentOverview(departmentOverview);

        List<TopEmployeesDTO> topEmployees = certificateEmployeeLookUpService.getTopEmployees(month, year);
        response.setEmployees(topEmployees);

        Map<String, Long> topDepartments = certificateEmployeeLookUpService.getTopDepartments(month, year);
        response.setTopDepartments(topDepartments);
        return response;
    }

    @Override
    public EmployeeMetricsResponseDTO getEmployeeOverAllStatistics(Integer month, Integer year) {
        log.info("Building Employee Overall Metrics for {}/{}", month, year);
        EmployeeMetricsResponseDTO response = new EmployeeMetricsResponseDTO();

        Long overAllDepartments = departmentLookUpService.getOverAllDepartmentsCount(month, year);
        response.setDepartments(overAllDepartments);

        Long overAllCertificates = certificateEmployeeLookUpService.getCertificatesCount(month, year);
        response.setCertificates(overAllCertificates);

        Long activeEmployees = employeeLookUpService.getActiveEmployeesCount(month, year);
        response.setEmployees(activeEmployees);

        Long categories = certificateCategoryLookUpService.getCertificateCategoriesCount(month, year);
        response.setCategory(categories);

        Long templates = templateLookUpService.getTemplateCount(month, year);
        response.setTemplate(templates);

        Long locations = locationLookUpService.getLocationCount(month, year);
        response.setLocation(locations);

        Long jobs = jobLookUpService.getJobsCount(month, year);
        response.setJobs(jobs);

        Long heads = certificateCommitteeLookUpService.getCommitteeHeadCount(month, year);
        response.setLocation(heads);

        Long members = certificateCommitteeLookUpService.getCommitteeMembersCount(month, year);
        response.setJobs(members);

        List<TopEmployeesDTO> topEmployees = certificateEmployeeLookUpService.getTopEmployees(month, year);
        response.setTopEmployees(topEmployees);

        Map<String, Long> topDepartments = certificateEmployeeLookUpService.getTopDepartments(month, year);
        response.setTopDepartments(topDepartments);
        return response;
    }
}
