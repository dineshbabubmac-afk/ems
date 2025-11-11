package com.annztech.rewardsystem.modules.employee.service.impl;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.common.security.dto.JwtDTO;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.common.utils.FileUtils;
import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.common.utils.ValidationUtils;
import com.annztech.rewardsystem.modules.appUser.event.EmployeeUserIdUpdateEvent;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.entity.CertificateEmployee;
import com.annztech.rewardsystem.modules.certificate.certificateemployee.service.CertificateEmployeeLookUpService;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import com.annztech.rewardsystem.modules.certificate.template.helper.TemplateHelper;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateLookUpService;
import com.annztech.rewardsystem.modules.common.constants.DomainConstants;
import com.annztech.rewardsystem.modules.common.helper.DomainHelper;
import com.annztech.rewardsystem.modules.department.dto.DepartmentDTO;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import com.annztech.rewardsystem.modules.employee.dto.*;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.event.NewEmployeeInviteEvent;
import com.annztech.rewardsystem.modules.employee.helper.EmployeeMapperHelper;
import com.annztech.rewardsystem.modules.employee.mapper.EmployeeMapper;
import com.annztech.rewardsystem.modules.employee.repository.EmployeeRepository;
import com.annztech.rewardsystem.modules.employee.service.EmployeeCompositeService;
import com.annztech.rewardsystem.modules.employee.service.EmployeeService;
import com.annztech.rewardsystem.modules.invite.event.UserDeleteInviteEvent;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.role.dto.RoleDTO;
import com.annztech.rewardsystem.modules.role.entity.Role;
import com.annztech.rewardsystem.modules.role.service.RoleLookUpService;
import com.annztech.rewardsystem.modules.tinyurl.dto.TinyUrlDTO;
import com.annztech.rewardsystem.modules.tinyurl.service.TinyUrlService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl extends LocalizationService implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeCompositeService employeeCompositeService;
    private final SequenceGeneratorRepository sequenceGenerator;
    private final ApplicationEventPublisher eventPublisher;
    private final CertificateEmployeeLookUpService certificateEmployeeLookUpService;
    private final RoleLookUpService roleLookUpService;
    private final TemplateLookUpService templateLookUpService;
    private final TinyUrlService tinyUrlService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                               EmployeeCompositeService employeeCompositeService, SequenceGeneratorRepository sequenceGenerator, ApplicationEventPublisher eventPublisher, CertificateEmployeeLookUpService certificateEmployeeLookUpService, RoleLookUpService roleLookUpService, TemplateLookUpService templateLookUpService, TinyUrlService tinyUrlService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeCompositeService = employeeCompositeService;
        this.sequenceGenerator = sequenceGenerator;
        this.eventPublisher = eventPublisher;
        this.certificateEmployeeLookUpService = certificateEmployeeLookUpService;
        this.roleLookUpService = roleLookUpService;
        this.templateLookUpService = templateLookUpService;
        this.tinyUrlService = tinyUrlService;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeDTO(String filter) {
        List<Employee> employees = getAllEmployees(filter);
        return employees.stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeDTOByDepartmentId(String departmentId) {
        if(StringUtils.isBlank(departmentId)){
            throw new AppException(getMessage(EmployeeConstants.EMPLOYEE_DEPT_ID_REQUIRED), HttpStatus.BAD_REQUEST);
        }
        if(!ValidationUtils.isValidUUID(departmentId)){
            throw new AppException(getMessage(EmployeeConstants.EMPLOYEE_DEPT_ID_INVALID), HttpStatus.BAD_REQUEST);
        }
        List<Employee> employees = employeeRepository.findEmployeeByDepartment_Id(UUID.fromString(departmentId));
        return employees.stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeDTOByRoles(String query) {
        List<Employee> employees = getAllEmployees(query);
        return getEmployeeRoles(employees);
    }

    @Override
    public List<EmployeeDTO> getEmployeeSearchByRoles(String query) {
        List<Employee> employees = employeeRepository.searchEmployees(query);
        return getEmployeeRoles(employees);
    }

    @Override
    public List<EmployeeDTO> getAllInternalEmployeeDTO() {
        List<Employee> employees = employeeRepository.findAll(DomainHelper.sortByUpdatedAtDesc());
        return employees.stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public EmployeeDTO getEmployeeDTOById(String id) {
        Employee employee = validateAndGetEmployee(id);
        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    public EmployeeIdResponseDTO createEmployeeDTO(EmployeeCreateDTO employeeCreateDTO) {
        JwtDTO jwtDTO = getJwtDTOFromSecurityContext();
        Department department = employeeCompositeService.getDepartmentById(employeeCreateDTO.getDepartmentId());
        Job job = employeeCompositeService.getJob(employeeCreateDTO.getJobId());
        Location location = employeeCompositeService.getLocationById(employeeCreateDTO.getLocationId());
        Employee employeeEntity = employeeMapper.toEntity(employeeCreateDTO);
        employeeEntity.setDepartment(department);
        employeeEntity.setJob(job);
        employeeEntity.setLocation(location);
        employeeEntity.setIsActive(true);
        employeeEntity.setIsDeleted(false);
        Role role = employeeCompositeService.getRoleEntityByName(EmployeeConstants.ROLE_EMPLOYEE);
        employeeEntity.setRole(role);
        employeeEntity.setEmployeeCode(getSequenceId(EmployeeConstants.EMPLOYEE_CODE_FORMART));
        Employee savedEntity = employeeRepository.save(employeeEntity);
        publishCreateEmployeeInvite(jwtDTO.getEmail(), employeeCreateDTO, department, job, location);
        return employeeMapper.toDto(savedEntity.getId().toString(), savedEntity.getEmployeeCode());
    }

    @Override
    public EmployeeIdResponseDTO updateEmployeeDTO(String id, EmployeeUpdateDTO employeeUpdateDTO) {
        Employee employeeEntity = validateAndGetEmployee(id);
        Employee updateEntity = EmployeeMapperHelper.updateEmployeeFromDTO(employeeEntity, employeeUpdateDTO);
        if (StringUtils.isNotBlank(employeeUpdateDTO.getDepartmentId())) {
            Department department = employeeCompositeService.getDepartmentById(employeeUpdateDTO.getDepartmentId());
            updateEntity.setDepartment(department);
        }
        if (StringUtils.isNotBlank(employeeUpdateDTO.getJobId())) {
            Job job = employeeCompositeService.getJob(employeeUpdateDTO.getJobId());
            updateEntity.setJob(job);
        }
        if (ValidationUtils.isValidId(employeeUpdateDTO.getLocationId())) {
            Location location = employeeCompositeService.getLocationById(employeeUpdateDTO.getLocationId());
            updateEntity.setLocation(location);
        }
        Employee savedEntity = employeeRepository.save(updateEntity);
        return employeeMapper.toDto(savedEntity.getId().toString(), savedEntity.getEmployeeCode());
    }

    @Override
    public EmployeeIdResponseDTO deleteEmployeeDTO(String id) {
        Employee employee = validateAndGetEmployee(id);
        employee.setIsDeleted(true);
        employee.setIsActive(false);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee.getId().toString(), employee.getEmployeeCode());
    }

    @Override
    public EmployeePicResponseDTO updateProfilePicDTO(String id, MultipartFile file) {
        Employee employee = validateAndGetEmployee(id);
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(FileUtils.IMAGE_DIR, fileName);
        try {
            FileUtils.createImageDirectoryIfNotExist();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            employee.setProfilePicturePath(filePath.toString());
            Employee savedEntity = employeeRepository.save(employee);
            return employeeMapper.toDto(savedEntity.getId().toString(), savedEntity.getEmployeeCode(), savedEntity.getProfilePicturePath());
        } catch (Exception e) {
            throw new AppException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Employee getEmployeeEntityByEmail(String email) {
        return employeeRepository.findByEmail(email.toLowerCase()).orElseThrow(() -> new AppException(getMessage(EmployeeConstants.EMPLOYEE_INVALID_EMAIL), HttpStatus.NOT_FOUND));
    }

    @Override
    public Employee getEmployeeEntityById(String id) {
        return validateAndGetEmployee(id);
    }

    private String getSequenceId(String sequenceCode){
        Long nextVal = sequenceGenerator.getNextSequence(EmployeeConstants.EMPLOYEE_CODE_SEQ);
        return String.format(sequenceCode, nextVal);
    }

    @Override
    public List<EmployeeDTO> getAllAdmins() {
        List<String> roleCodes = List.of(
                EmployeeConstants.ROLE_ADMIN,
                EmployeeConstants.ROLE_SUPER_ADMIN
        );
        List<Role> roles = roleLookUpService.getCode(roleCodes);
        List<UUID> roleIds = roles.stream().map(Role::getId).toList();
        List<Employee> employees = employeeRepository.findByRoleIds(roleIds, DomainHelper.sortByUpdatedAtDesc());
        log.info("Fetched all users with size: {}", employees.size());
        return employees.stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public List<EmployeeRewardsResponseDTO> getAllMyRewards() {
        String email = DomainHelper.getUserEmail();
        List<CertificateEmployee> rewards = certificateEmployeeLookUpService.getAllRewards(email);
        log.info("Fetched all rewards with size: {}", rewards.size());
        return rewards.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getSearchByUsingFirstNameOrEmail(String query, String roleCode) {
        List<Employee> employees = employeeRepository.searchEmployee(searchNullCheckQuery(query), roleCode);
        log.info("Found {} employees for query '{}'", employees.size(), query); // log the result count
        return employees.stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public List<DepartmentDTO> getSearchByUsingDepartmentOrEmployeeCode(String query, String roleCode) {
        List<Employee> employees = employeeRepository.searchEmployeeByDepartmentOrEmployeeId(searchNullCheckQuery(query), roleCode);
        log.info("Found {} employees for query '{}'", employees.size(), query); // log the result count
        return employees.stream().map(DepartmentDTO::new).toList();
    }

    @Override
    public String getCertificateTemplateViewById(String templateId, String id) {
        try {
            StringsUtils.validateUUID(id, "Invalid Employee id: " + id);
            UUID employeeUuid = UUID.fromString(id);
            Template template = templateLookUpService.getTemplateById(UUID.fromString(templateId));
            log.info("Fetching certificate for template: {} and employee id: {}", templateId, id);
            CertificateEmployee certificateEmployee = certificateEmployeeLookUpService.getCertificateEmployeeByTemplateIdAndEmployee(UUID.fromString(templateId), employeeUuid);
            String logo = EmployeeConstants.LOGO;
            String employeeName = certificateEmployee.getEmployee().getFirstName().toUpperCase() + " " + certificateEmployee.getEmployee().getLastName().toUpperCase();
            String certificateName = certificateEmployee.getCertificate().getName();
            String empCode = certificateEmployee.getEmployee().getEmployeeCode();
            String issueDate = DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneOffset.UTC).format(certificateEmployee.getCreatedAt());
            Map<String, String> variables = Map.of(
                    "logo", logo,
                    "name", employeeName,
                    "certificateName", certificateName,
                    "date", issueDate,
                    "empCode", empCode
            );
            return TemplateHelper.loadCertificateTemplate("templates/" + template.getPath() + ".html", variables);
        } catch (Exception e) {
            log.error("Error rendering certificate template [{}]: {}", id, e.getMessage(), e);
            throw new AppException("Failed to render certificate template", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private EmployeeRewardsResponseDTO mapToResponseDTO(CertificateEmployee ce) {
        EmployeeRewardsResponseDTO dto = EmployeeRewardsResponseDTO.builder()
                .name(ce.getCertificate().getName())
                .description(ce.getCertificate().getDescription())
                .date(ce.getCreatedAt().toString())
                .category(ce.getCertificate().getCertificateCategory().getNameEnglish())
                .empCode(ce.getEmployee().getEmployeeCode())
                .certificateName(ce.getCertificate().getName())
                .build();
        try {
            String viewUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/employees/")
                    .path(ce.getCertificate().getCertificateTemplate().getId().toString())
                    .path("/view")
                    .queryParam("id", ce.getEmployee().getId())
                    .toUriString();
            TinyUrlDTO urlDTO = tinyUrlService.createEmployeeTinyUrl(viewUrl);
            dto.setTemplateURL(urlDTO.getTinyUrl());
        } catch (Exception e) {
            log.error("Error generating template URL for certificate employee [{}]: {}", ce.getId(), e.getMessage());
            dto.setTemplateURL(null);
        }
        return dto;
    }

    @Override
    public List<EmployeeDTO> getSearchEmployeeDTO(String query) {
            List<Employee> employees = employeeRepository.searchEmployees(query);
            return employees.stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public void activateEmployeeEntityByLocationId(Long locationId) {
        List<Employee> employees = employeeRepository.findEmployeesByLocation_Id(locationId).orElse(Collections.emptyList());
        updateEmployeeState(employees, true, false);
    }

    @Override
    public void deactivateEmployeeEntityByLocationId(Long locationId) {
        List<Employee> employees = employeeRepository.findEmployeesByLocation_Id(locationId).orElse(Collections.emptyList());
        updateEmployeeState(employees, false, true);
    }


    @Override
    public void activateEmployeeEntityByJobId(String jobId) {
        StringsUtils.validateUUID(jobId, EmployeeConstants.EMPLOYEE_JOB_ID_REQUIRED);
        UUID jobUuid = UUID.fromString(jobId);
        List<Employee> employees = employeeRepository.findEmployeeByJob_Id(jobUuid);
        updateEmployeeState(employees, true, false);
    }

    @Override
    public void deactivateEmployeeEntityByJobId(String jobId) {
        StringsUtils.validateUUID(jobId, EmployeeConstants.EMPLOYEE_JOB_ID_REQUIRED);
        UUID jobUuid = UUID.fromString(jobId);
        List<Employee> employees = employeeRepository.findEmployeeByJob_Id(jobUuid);
        updateEmployeeState(employees, false, true);
    }

    @Override
    public void activateEmployeeEntityByDepartmentId(String departmentId) {
        StringsUtils.validateUUID(departmentId, EmployeeConstants.EMPLOYEE_DEPT_ID_INVALID);
        UUID depUuid = UUID.fromString(departmentId);
        List<Employee> employees = employeeRepository.findEmployeeByDepartment_Id(depUuid);
        updateEmployeeState(employees, true, false);
    }

    @Override
    public void deactivateEmployeeEntityByDepartmentId(String departmentId) {
        StringsUtils.validateUUID(departmentId, EmployeeConstants.EMPLOYEE_DEPT_ID_INVALID);
        UUID depUuid = UUID.fromString(departmentId);
        List<Employee> employees = employeeRepository.findEmployeeByDepartment_Id(depUuid);
        updateEmployeeState(employees, false, true);
    }

    private void publishCreateEmployeeInvite(String senderMail, EmployeeCreateDTO employeeCreateDTO, Department department, Job job, Location location) {
        EmployeeInviteDTO employeeInviteDTO = EmployeeInviteDTO.builder()
                .sendersEmail(senderMail.toLowerCase())
                .receiverEmail(employeeCreateDTO.getEmail().toLowerCase())
                .referenceId(department.getId().toString())
                .name(employeeCreateDTO.getFirstName() + " " + employeeCreateDTO.getLastName())
                .appType(DomainConstants.EMPLOYEE_REWARD_APP)
                .departmentName(department.getNameEn())
                .jobTitle(job.getTitleEn())
                .locationName(location.getNameEn())
                .build();
        eventPublisher.publishEvent(new NewEmployeeInviteEvent(employeeInviteDTO));
    }

    //New Employee onboard Event publisher
    private Employee validateAndGetEmployee(String id) {
        if (!ValidationUtils.isValidUUID(id)) {
            throw new AppException(getMessage(EmployeeConstants.EMPLOYEE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        return employeeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(getMessage(EmployeeConstants.EMPLOYEE_NO_RECORDS), HttpStatus.NOT_FOUND));
    }

    private Department validateAndGetDepartment(String id) {
        if (!ValidationUtils.isValidUUID(id)) {
            throw new AppException(getMessage(EmployeeConstants.EMPLOYEE_INVALID_ID), HttpStatus.BAD_REQUEST);
        }
        return employeeCompositeService.getDepartmentById(id);
    }

    private void updateEmployeeState(List<Employee> employees, boolean active, boolean deleted) {
        if (employees.isEmpty()) {
            return;
        }
        employees.forEach(employee -> {
            employee.setIsActive(active);
            employee.setIsDeleted(deleted);
        });
        employeeRepository.saveAll(employees);
    }

    private void updateEmployeeRoles(UUID empId, Employee employeeEntity, Role newRole) {
        employeeEntity.setRole(newRole);
        employeeRepository.save(employeeEntity);
    }

    private JwtDTO getJwtDTOFromSecurityContext(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (JwtDTO) authentication.getPrincipal();
    }

    private List<EmployeeDTO> getEmployeeRoles(List<Employee> employees) {
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for(Employee employee : employees){
            EmployeeDTO employeeDTO = new EmployeeDTO();
            RoleDTO roleDTO = new RoleDTO(employee.getRole());
            employeeDTO.setFullName(StringsUtils.capitalizeName(employee.getFirstName()) + " " + (StringsUtils.capitalizeName(employee.getLastName())));
            employeeDTO.setId(employee.getId().toString());
            employeeDTO.setLastLogin(employee.getLastLoginAt());
            employeeDTO.setEmail(employee.getEmail());
            employeeDTO.setDepartmentName(employee.getDepartment().getNameEn());
            employeeDTO.setActive(employee.getIsActive());
            employeeDTO.setRole(roleDTO);
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;
    }

    private List<Employee> getAllEmployees(String filter) {
        List<Employee> employees;
        if (StringUtils.isNotBlank(filter)) {
            String normalizedFilter = filter.trim().toLowerCase();
            if (normalizedFilter.contains("inactive")) {
                employees = employeeRepository.findAllInActiveEmployees(DomainHelper.sortByCreatedAtDesc());
            } else if (normalizedFilter.contains("all")) {
                employees = employeeRepository.findAll(DomainHelper.sortByCreatedAtDesc());
            } else {
                employees = employeeRepository.findAllActiveEmployees(DomainHelper.sortByCreatedAtDesc());
            }
        } else {
            employees = employeeRepository.findAllActiveEmployees(DomainHelper.sortByCreatedAtDesc());
        }
        return employees;
    }

    @EventListener
    @Transactional
    public void onEmployeeUserIdUpdateEvent(EmployeeUserIdUpdateEvent event){
        Employee employee = employeeRepository.findByEmail(event.getEmail()).orElseThrow(() -> new AppException(getMessage(EmployeeConstants.EMPLOYEE_INVALID_EMAIL), HttpStatus.NOT_FOUND));
        employee.setUserId(event.getUserId());
        employeeRepository.save(employee);
    }

    @EventListener
    @Transactional
    public void deleteEmployeeInvite(UserDeleteInviteEvent event){
        Employee employee = employeeRepository.findByEmail(event.getEmail().toLowerCase()).orElseThrow(() -> new AppException(getMessage(EmployeeConstants.EMPLOYEE_INVALID_EMAIL), HttpStatus.NOT_FOUND));
        employeeRepository.delete(employee);
    }
    private String searchNullCheckQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        return query.trim();
    }
}