package com.annztech.rewardsystem.seeder;

import com.annztech.rewardsystem.common.repsoitory.SequenceGeneratorRepository;
import com.annztech.rewardsystem.modules.appUser.constants.AppUserConstants;
import com.annztech.rewardsystem.modules.appUser.entity.AppUser;
import com.annztech.rewardsystem.modules.appUser.repository.AppUserRepository;
import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.department.repository.DepartmentRepository;
import com.annztech.rewardsystem.modules.employee.constants.EmployeeConstants;
import com.annztech.rewardsystem.modules.employee.entity.Employee;
import com.annztech.rewardsystem.modules.employee.repository.EmployeeRepository;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.job.repository.JobRepository;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.location.repository.LocationRepository;
import com.annztech.rewardsystem.modules.lookups.entity.BandLevelLookup;
import com.annztech.rewardsystem.modules.lookups.repository.BandLevelLookUpRepository;
import com.annztech.rewardsystem.modules.role.entity.Role;
import com.annztech.rewardsystem.modules.role.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final DepartmentRepository departmentRepository;
    private final JobRepository jobRepository;
    private final LocationRepository locationRepository;
    private final RoleRepository roleRepository;
    private final BandLevelLookUpRepository bandLevelLookUpRepository;
    private final SequenceGeneratorRepository sequenceNumberGenerator;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        InputStream excelFile = getClass().getClassLoader().getResourceAsStream("data/reward-system-dumps.xlsx");
        if (excelFile == null) {
            System.out.println("⚠️ reward-system-dumps.xlsx not found in resources. Skipping user import.");
            return;
        }
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet users = workbook.getSheet("Users");
        Sheet departments = workbook.getSheet("Department");
        Sheet jobs = workbook.getSheet("Job");
        Sheet locations = workbook.getSheet("Location");

        seedOwnerDepartment(departments);
        seedOwnerJobTitle(jobs);
        seedOwnerLocation(locations);
        seedOwnerAppUser(users);
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK &&
                    !new DataFormatter().formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void seedOwnerDepartment(Sheet departmentSheet) {
        for (Row row : departmentSheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if (isRowEmpty(row)) continue;
            String nameEn = row.getCell(0).getStringCellValue();
            String nameAr = row.getCell(1).getStringCellValue();
            if (!departmentRepository.existsDepartmentByNameEn(nameEn) && !departmentRepository.existsDepartmentByNameAr(nameAr)) {
                Department department = new Department();
                department.setCreatedAt(Instant.now());
                department.setUpdatedAt(Instant.now());
                department.setNameAr(nameAr);
                department.setNameEn(nameEn);
                departmentRepository.save(department);
                System.out.println("✅ Department is created.");
            };
        }
    }

    public void seedOwnerJobTitle(Sheet jobSheet) {
        for (Row row : jobSheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if (isRowEmpty(row)) continue;
            String titleEn = row.getCell(0).getStringCellValue();
            String titleAr = row.getCell(1).getStringCellValue();
            String band = row.getCell(2).getStringCellValue();
            if (!jobRepository.existsJobByTitleEn(titleEn)
                    && !jobRepository.existsJobByTitleAr(titleAr)){
                Optional<BandLevelLookup> bandLevelLookup = bandLevelLookUpRepository.findByCode(band);
                Job job = new Job();
                job.setCreatedAt(Instant.now());
                job.setUpdatedAt(Instant.now());
                job.setTitleEn(titleEn);
                job.setTitleAr(titleAr);
                bandLevelLookup.ifPresent(job::setBandLevel);
                jobRepository.save(job);
                System.out.println("✅ Job Title is created.");
            };
        }
    }

    public void seedOwnerLocation(Sheet locationSheet) {
        for (Row row : locationSheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if (isRowEmpty(row)) continue;
            String nameEn = row.getCell(0).getStringCellValue();
            String nameAr = row.getCell(1).getStringCellValue();
            if (!locationRepository.existsLocationByNameEn(nameEn)
                    && !locationRepository.existsLocationByNameAr(nameAr)){
                Location location = new Location();
                location.setCreatedAt(Instant.now());
                location.setUpdatedAt(Instant.now());
                location.setNameAr(nameAr);
                location.setNameEn(nameEn);
                locationRepository.save(location);
                System.out.println("✅ Location is created.");
            }
        }
    }


    public void seedOwnerAppUser(Sheet userSheet) {
        DataFormatter formatter = new DataFormatter();
        for (Row row : userSheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if (isRowEmpty(row)) continue;
            String email = row.getCell(0).getStringCellValue();
            String firstName = row.getCell(1).getStringCellValue();
            String lastName = row.getCell(2).getStringCellValue();
            String mobile = formatter.formatCellValue(row.getCell(3));
            String gender = row.getCell(4).getStringCellValue();
            String password = row.getCell(5).getStringCellValue();
            String role = row.getCell(6).getStringCellValue();
            String department = row.getCell(7).getStringCellValue();
            String job = row.getCell(8).getStringCellValue();
            String location = row.getCell(9).getStringCellValue();
            String appCode = row.getCell(10).getStringCellValue();
            if (!appUserRepository.existsByEmail(email)) {
                AppUser appUser = new AppUser();
                appUser.setEmail(email);
                appUser.setPassword(passwordEncoder.encode(password));
                appUser.setIsActive(true);
                appUser.setStatus("PENDING");
                appUser.setAppCode(appCode);
                Long nextVal = sequenceNumberGenerator.getNextSequence(AppUserConstants.USER_CODE_SEQ);
                String code = String.format(AppUserConstants.USER_CODE_FORMAT, nextVal);
                appUser.setUserCode(code);
                AppUser savedAppUser = appUserRepository.save(appUser);
                System.out.println("✅ App user is created.");
                seedOwnerEmployee(savedAppUser, firstName, lastName, role, mobile, gender, department, job, location);
            };
        }
    }

    public void seedOwnerEmployee(AppUser appUser,
                                  String firstName,
                                  String lastName,
                                  String roleName,
                                  String mobileNumber,
                                  String gender,
                                  String departmentName,
                                  String jobTitle,
                                  String locationName) {
        if (!employeeRepository.existsEmployeeByEmail(appUser.getEmail())) {
            Department department = departmentRepository.findDepartmentByNameEn(departmentName).orElse(null);
            Job job = jobRepository.findJobByTitleEn(jobTitle).orElse(null);
            Location location = locationRepository.findLocationByNameEn(locationName).orElse(null);
            Role role = roleRepository.getRoleByCode(roleName).orElse(null);
            System.out.println(" 👉 Department name = " + departmentName);
            System.out.println(" 👉 Job title = " + jobTitle);
            System.out.println(" 👉 Location name = " + locationName);
            System.out.println(" 👉 Role name = " + roleName);
            if (department == null || job == null || location == null || role == null) {
                System.out.println(" ❌ department or Job or location should not be null.");
                System.out.println(" 🚫 Stopped seeding");
                return;
            }
            Employee portalOwner = new Employee();
            portalOwner.setCreatedAt(Instant.now());
            portalOwner.setRole(role);
            portalOwner.setUserId(appUser.getId());
            portalOwner.setUpdatedAt(Instant.now());
            portalOwner.setEmail(appUser.getEmail().toLowerCase());
            portalOwner.setDepartment(department);
            portalOwner.setJob(job);
            portalOwner.setLocation(location);
            portalOwner.setLocation(location);
            portalOwner.setFirstName(firstName.toLowerCase());
            portalOwner.setLastName(lastName.toLowerCase());
            portalOwner.setMobileNumber(mobileNumber);
            portalOwner.setGender(gender.equalsIgnoreCase("Male") ? 1 : 0);
            Long nextVal = sequenceNumberGenerator.getNextSequence(EmployeeConstants.EMPLOYEE_CODE_SEQ);
            String code = String.format(EmployeeConstants.EMPLOYEE_CODE_FORMART, nextVal);
            portalOwner.setEmployeeCode(code);
            employeeRepository.save(portalOwner);
            System.out.println("✅ Employee is created.");
        }
    }
}
