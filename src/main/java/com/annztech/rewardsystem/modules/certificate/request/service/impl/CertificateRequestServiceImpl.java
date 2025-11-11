package com.annztech.rewardsystem.modules.certificate.request.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.dto.CertificateApprovalInfoDTO;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.service.CertificateRequestApprovalLookupService;
import com.annztech.rewardsystem.modules.certificate.approval.workflow.service.CertificateRequestApprovalService;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.dto.CertificateEmployeeResponseDTO;
import com.annztech.rewardsystem.modules.certificate.criteria.entity.Criterion;
import com.annztech.rewardsystem.modules.certificate.criteria.repository.CriteriaRepository;
import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import com.annztech.rewardsystem.modules.certificate.request.constants.CertificateRequestConstant;
import com.annztech.rewardsystem.modules.certificate.request.dto.*;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequest;
import com.annztech.rewardsystem.modules.certificate.request.entity.CertificateRequestCriterion;
import com.annztech.rewardsystem.modules.certificate.request.repository.CertificateRequestCriterionRepository;
import com.annztech.rewardsystem.modules.certificate.request.repository.CertificateRequestRepository;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateEmployeeAssignmentService;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestAssignmentService;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestCompositeService;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestService;
import com.annztech.rewardsystem.modules.certificate.status.entity.CertificateStatus;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.drives.attachments.dto.AttachmentDTO;
import com.annztech.rewardsystem.modules.drives.dto.DriveDTO;
import com.annztech.rewardsystem.modules.drives.folders.dto.FolderDTO;
import com.annztech.rewardsystem.modules.drives.service.DriveService;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.event.BulkCertificateRequestAssignmentEvent;
import com.annztech.rewardsystem.modules.employee.event.CertificateRequestAssignmentEvent;
import com.annztech.rewardsystem.modules.lookups.service.CertificateLookUpService;
import com.annztech.rewardsystem.modules.lookups.service.CertificateStatusLookUpService;
import com.annztech.rewardsystem.modules.lookups.service.EmployeeLookUpService;
import com.annztech.rewardsystem.modules.reports.dto.PagedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CertificateRequestServiceImpl extends LocalizationService implements CertificateRequestService {
    private final SequenceGeneratorRepository sequenceGenerator;
    private final EmployeeLookUpService employeeLookUpService;
    private final CertificateLookUpService certificateLookUpService;
    private final CertificateStatusLookUpService certificateStatusLookUpService;
    private final CertificateRequestCompositeService certificateRequestCompositeService;
    private final CertificateRequestAssignmentService certificateRequestAssignmentService;
    private final CertificateRequestRepository certificateRequestRepository;
    private final CertificateRequestCriterionRepository certificateRequestCriterionRepository;
    private final CriteriaRepository criteriaRepository;
    private final CertificateEmployeeAssignmentService certificateEmployeeAssignmentService;
    private final CertificateRequestApprovalLookupService certificateRequestApprovalLookupService;
    private final DriveService driveService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper objectMapper;

    public CertificateRequestServiceImpl(SequenceGeneratorRepository sequenceGenerator, EmployeeLookUpService employeeLookUpService, CertificateLookUpService certificateLookUpService, CertificateStatusLookUpService certificateStatusLookUpService, CertificateRequestCompositeService certificateRequestCompositeService, CertificateRequestAssignmentService certificateRequestAssignmentService, CertificateRequestRepository certificateRequestRepository, CertificateRequestCriterionRepository certificateRequestCriterionRepository, CriteriaRepository criteriaRepository, CertificateEmployeeAssignmentService certificateEmployeeAssignmentService, CertificateRequestApprovalLookupService certificateRequestApprovalLookupService, DriveService driveService, ApplicationEventPublisher applicationEventPublisher, ObjectMapper objectMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.employeeLookUpService = employeeLookUpService;
        this.certificateLookUpService = certificateLookUpService;
        this.certificateStatusLookUpService = certificateStatusLookUpService;
        this.certificateRequestCompositeService = certificateRequestCompositeService;
        this.certificateRequestAssignmentService = certificateRequestAssignmentService;
        this.certificateRequestRepository = certificateRequestRepository;
        this.certificateRequestCriterionRepository = certificateRequestCriterionRepository;
        this.criteriaRepository = criteriaRepository;
        this.certificateEmployeeAssignmentService = certificateEmployeeAssignmentService;
        this.certificateRequestApprovalLookupService = certificateRequestApprovalLookupService;
        this.driveService = driveService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public CertificateRequestIdResponseDTO createCertificateRequest(CertificateRequestCreateDTO dto) {
        List<CertificateRequestCriteriaDTO> criteriaList = getCriteriaList(dto);
        String userId = DomainHelper.getId();
        Employee byEmployee = employeeLookUpService.getEmployeeById(userId);
        Employee toEmployee = employeeLookUpService.getEmployeeById(dto.getNominatedToEmployee());
        Certificate certificate = certificateLookUpService.getCertificateById(dto.getCertificateId());
        CertificateStatus certificateStatus = certificateStatusLookUpService.getCertificateStatusCode("PENDING");
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setActionedAt(Instant.now());
        certificateRequest.setNominatedBy(byEmployee);
        certificateRequest.setNominatedTo(toEmployee);
        certificateRequest.setCertificate(certificate);
        certificateRequest.setStatusCode(certificateStatus);
        certificateRequest.setCode(getSequenceId());
        certificateRequest.setIsActive(true);
        certificateRequest.setIsDeleted(false);
        CertificateRequest savedCertificateRequest = certificateRequestRepository.save(certificateRequest);
        if(!criteriaList.isEmpty()){
            saveCertificateRequestCriteria(criteriaList, savedCertificateRequest);
        }
        List<Employee> allParticularCommitteeMembers = certificateRequestCompositeService.getAllParticularCommitteeMembers();
        certificateRequestAssignmentService.certificateRequestAssignedToCommitteeMembers(allParticularCommitteeMembers, savedCertificateRequest, certificateStatus);
        publishCertificateRequestAssignmentEvent(allParticularCommitteeMembers, savedCertificateRequest, byEmployee, toEmployee, certificate);
        return new CertificateRequestIdResponseDTO(savedCertificateRequest.getCode());
    }

    private List<CertificateRequestCriteriaDTO> getCriteriaList(CertificateRequestCreateDTO dto) {
        List<CertificateRequestCriteriaDTO> criteriaList = new ArrayList<>();
        try {
            if (dto != null && dto.getCriteriaJSONString() != null && !dto.getCriteriaJSONString().isEmpty()) {
                criteriaList = objectMapper.readValue(
                        dto.getCriteriaJSONString(),
                        new TypeReference<List<CertificateRequestCriteriaDTO>>() {}
                );
                List<MultipartFile> files = dto.getFiles();
                if (files != null && !files.isEmpty()) {
                    for (int i = 0; i < criteriaList.size(); i++) {
                        if (i < files.size()) {
                            criteriaList.get(i).setFile(files.get(i));
                        } else {
                            log.warn("File list smaller than criteria list — no file for index {}", i);
                        }
                    }
                } else {
                    log.warn("File list is null or empty, skipping file assignment");
                }
            } else {
                log.warn("Criteria JSON string is null or empty");
            }
        } catch (Exception e) {
            log.error("Error while parsing criteria JSON string", e);
        }
        return criteriaList;
    }

    private void saveCertificateRequestCriteria(List<CertificateRequestCriteriaDTO> criteriaList, CertificateRequest certificateRequest) {
        List<CertificateRequestCriterion> certificateRequestCriteriaList = new ArrayList<>();
        String folderId = driveService.getFolderId(CertificateRequestConstant.CERTIFICATE_REQUEST, certificateRequest.getId().toString());
        String userId = DomainHelper.getId();
        DriveDTO driveDTO = new DriveDTO();
        driveDTO.setReferencedId(certificateRequest.getId().toString());
        driveDTO.setReferencedType(CertificateRequestConstant.CERTIFICATE_REQUEST);
        driveDTO.setFolderId(folderId);
        Map<String, MultipartFile> files = criteriaList.stream().collect(Collectors.toMap(CertificateRequestCriteriaDTO::getId, CertificateRequestCriteriaDTO::getFile));
        driveDTO.setFiles(files);
        driveDTO.setUploadedBy(UUID.fromString(userId));
        for (CertificateRequestCriteriaDTO criteria : criteriaList) {
            UUID criterionUUID = UUID.fromString(criteria.getId());
            Criterion criterion = criteriaRepository.findCriteriaById(criterionUUID);
            if (criterion != null) {
                CertificateRequestCriterion certificateRequestCriterion = new CertificateRequestCriterion();
                certificateRequestCriterion.setCertificateRequest(certificateRequest);
                certificateRequestCriterion.setCriteria(criterion);
                certificateRequestCriterion.setRemarks(criteria.getRemarks());
                certificateRequestCriteriaList.add(certificateRequestCriterion);
            }
        }
        certificateRequestCriterionRepository.saveAll(certificateRequestCriteriaList);
        driveService.createDriveAndUpload(driveDTO);
    }

    private void publishCertificateRequestAssignmentEvent(List<Employee> committeeMembers, CertificateRequest certificateRequest, Employee nominatedBy, Employee nominatedTo, Certificate certificate) {
        if (committeeMembers == null || committeeMembers.isEmpty()) {
            log.warn("⚠️ No committee members found to notify");
            return;
        }
        List<CertificateRequestAssignmentEvent> assignmentEvents = new ArrayList<>();
        String nominatedByName = nominatedBy.getFirstName() + " " + nominatedBy.getLastName();
        String nominatedToName = nominatedTo.getFirstName() + " " + nominatedTo.getLastName();
        String certificateName = certificate.getName();
        String certificateCategory = certificate.getCertificateCategory() != null ? certificate.getCertificateCategory().getNameEnglish() : "N/A";
        for (Employee member : committeeMembers) {
            String memberName = member.getFirstName() + " " + member.getLastName();
            CertificateRequestAssignmentEvent event = new CertificateRequestAssignmentEvent();
            event.setCommitteeMemberEmail(member.getEmail());
            event.setCommitteeMemberId(member.getId().toString());
            event.setCommitteeMemberName(memberName);
            event.setNominatedByName(nominatedByName);
            event.setNominatedToName(nominatedToName);
            event.setCertificateName(certificateName);
            event.setRequestCode(certificateRequest.getCode());
            event.setCertificateCategory(certificateCategory);
            assignmentEvents.add(event);
        }
        applicationEventPublisher.publishEvent(new BulkCertificateRequestAssignmentEvent(assignmentEvents));
        log.info("✅ Published BulkCertificateRequestAssignmentEvent for {} committee members", assignmentEvents.size());
    }

    @Override
    public PagedResponse<CertificateRequestResponseDTO> getAllCertificateRequests(String statusCode, Pageable pageable) {
        Page<CertificateRequest> page;
        if(statusCode == null){
            page = certificateRequestRepository.findAll(pageable);
        }else{
            page = certificateRequestRepository.findAllByStatusCode_Code(statusCode, pageable);
        }
        List<CertificateRequestResponseDTO> dtoList = page.getContent().stream().map(CertificateRequestResponseDTO::new).toList();
        return new PagedResponse<>(
                dtoList,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public CertificateRequestIdResponseDTO updateCertificateRequest(String certificateRequestId, CertificateRequestUpdateDTO dto) {
        StringsUtils.validateUUID(certificateRequestId, CertificateRequestConstant.CERTIFICATE_REQUEST_INVALID_ID + certificateRequestId);
        CertificateRequest certificateRequest = certificateRequestRepository.findById(UUID.fromString(certificateRequestId)).orElseThrow(() -> new AppException(CertificateRequestConstant.CERTIFICATE_REQUEST_NO_RECORDS, HttpStatus.NOT_FOUND));
        if (dto.getNominatedByEmployee() != null) {
            Employee byEmployee = employeeLookUpService.getEmployeeById(dto.getNominatedByEmployee());
            certificateRequest.setNominatedBy(byEmployee);
        }
        if (dto.getNominatedToEmployee() != null) {
            Employee toEmployee = employeeLookUpService.getEmployeeById(dto.getNominatedToEmployee());
            certificateRequest.setNominatedTo(toEmployee);
        }
        if (dto.getCertificateId() != null) {
            Certificate certificate = certificateLookUpService.getCertificateById(dto.getCertificateId());
            certificateRequest.setCertificate(certificate);
        }
        if (dto.getStatusCode() != null) {
            CertificateStatus certificateStatus = certificateStatusLookUpService.getCertificateStatusCode(dto.getStatusCode());
            certificateRequest.setStatusCode(certificateStatus);
        }
        if (dto.getRemark() != null) {
            certificateRequest.setRemarks(dto.getRemark());
        }
        certificateRequest.setActionedAt(Instant.now());
        CertificateRequest updated = certificateRequestRepository.save(certificateRequest);
        return new CertificateRequestIdResponseDTO(updated.getCode());
    }

    @Override
    public CertificateRequestIdResponseDTO deleteCertificateRequest(String id) {
        StringsUtils.validateUUID(id, CertificateRequestConstant.CERTIFICATE_REQUEST_INVALID_ID + id);
        CertificateRequest certificateRequest = certificateRequestRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(CertificateRequestConstant.CERTIFICATE_REQUEST_NO_RECORDS, HttpStatus.NOT_FOUND));
        certificateRequest.setIsDeleted(true);
        certificateRequest.setIsActive(false);
        return new CertificateRequestIdResponseDTO(certificateRequest.getCode());
    }

    @Override
    public CertificateRequestResponseDTO getCertificateRequestById(String requestId) {
        StringsUtils.validateUUID(requestId, CertificateRequestConstant.CERTIFICATE_REQUEST_INVALID_ID + requestId);
        CertificateRequest certificateRequest = certificateRequestRepository.findById(UUID.fromString(requestId)).orElseThrow(() -> new AppException(CertificateRequestConstant.CERTIFICATE_REQUEST_NO_RECORDS, HttpStatus.NOT_FOUND));
        List<CertificateRequestCriterion> certificateRequestCriteria = certificateRequestCriterionRepository.findCertificateRequestCriteriaByCertificateRequest_Id(UUID.fromString(requestId));
        List<CriteriaRemarkDTO> criteriaRemarkDTOList = certificateRequestCriteria.stream().map(CriteriaRemarkDTO::new).toList();
        List<AttachmentDTO> attachments = driveService.getAttachment(CertificateRequestConstant.CERTIFICATE_REQUEST, requestId);

        Map<String, String> attachmentMap = attachments.stream()
                .collect(Collectors.toMap(
                        att -> extractCriteriaId(att.getFileUrl()),
                        AttachmentDTO::getFileUrl,
                        (a, b) -> a
                ));
        log.info("attachments : {}", attachmentMap);

        criteriaRemarkDTOList.forEach(dto -> {
            String criteriaId = dto.getCriteriaId();
            String fileUrl = attachmentMap.get(criteriaId);
            log.info("criteriaId : {}, fileUrl : {}", criteriaId, fileUrl);
            dto.setFileUrl(fileUrl);
        });
        List<CertificateApprovalInfoDTO> approvalInfoDTOS = certificateRequestApprovalLookupService.getCertificateApprovalInfo(requestId);
        CertificateRequestResponseDTO certificateRequestResponseDTO = new CertificateRequestResponseDTO(certificateRequest);
        certificateRequestResponseDTO.setCriteriaList(criteriaRemarkDTOList);
        certificateRequestResponseDTO.setCertificateApprovalDetails(approvalInfoDTOS);
        return certificateRequestResponseDTO;
    }

    @Override
    public List<CertificateRequestResponseDTO> search(String search, String actionField, String status) {
        boolean action = false;
        if(actionField != null){
            if(actionField.equalsIgnoreCase("ACTIVE")){
                action = true;
            }
        }
        List<CertificateRequest> certificateRequests = certificateRequestRepository.search(searchNullCheckQuery(search), action, status);
        log.info("certificate requests : {}", certificateRequests);
        return certificateRequests.stream().map(CertificateRequestResponseDTO::new).toList();
    }

    @Override
    public CertificateRequestResponseDTO certificatePublish(CertificateRequestPublishDTO certificateRequestPublishDTO) {
        StringsUtils.validateUUID(certificateRequestPublishDTO.getCertificateId(), CertificateRequestConstant.CERTIFICATE_REQUEST_INVALID_ID + certificateRequestPublishDTO.getCertificateId());
        if(!DomainHelper.checkSuperAdmin()){
            throw new AppException(CertificateRequestConstant.CERTIFICATE_REQUEST_NOT_ALLOWED_PUBLISHED, HttpStatus.BAD_REQUEST);
        }
        CertificateRequest certificateRequest = certificateRequestRepository.findById(UUID.fromString(certificateRequestPublishDTO.getCertificateId())).orElseThrow(() -> new AppException(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_NO_RECORDS), HttpStatus.NOT_FOUND));
        certificateRequest.setStatusCode(certificateStatusLookUpService.getCertificateStatusCode("PUBLISHED"));
        certificateEmployeeAssignmentService.publishCertificate(certificateRequest);
        return new CertificateRequestResponseDTO(certificateRequest);
    }

    /**
     * Extracts a UUID from the given file URL.
     * Assumes the UUID appears anywhere in the URL.
     */
    private String extractCriteriaId(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        // Match UUID before file extension at the end of the string
        Matcher matcher = Pattern.compile(
                "([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})(?=\\.[a-zA-Z0-9]+$)"
        ).matcher(fileUrl);

        return matcher.find() ? matcher.group(1) : null;
    }

    private String searchNullCheckQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        return query.trim();
    }
    private String getSequenceId(){
        Long nextVal = sequenceGenerator.getNextSequence(CertificateRequestConstant.CERTIFICATE_REQUEST_CODE_SEQ);
        return String.format(CertificateRequestConstant.CERTIFICATE_REQUEST_CODE_FORMAT, nextVal);
    }
}
