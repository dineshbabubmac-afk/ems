package com.annztech.rewardsystem.modules.employee.entity;

import com.annztech.rewardsystem.modules.department.entity.Department;
import com.annztech.rewardsystem.modules.job.entity.Job;
import com.annztech.rewardsystem.modules.location.entity.Location;
import com.annztech.rewardsystem.modules.role.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Size(max = 20)
    @NotNull
    @Column(name = "employee_code", nullable = false, length = 20)
    private String employeeCode;

    @Column(name = "user_id")
    private UUID userId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "profile_picture_path", length = Integer.MAX_VALUE)
    private String profilePicturePath;

    @Size(max = 50)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 20)
    @NotNull
    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Size(max = 254)
    @NotNull
    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @NotNull
    @Column(name = "gender", nullable = false)
    private Integer gender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        lastLoginAt = Instant.now();
        if (email != null) {
            email = email.toLowerCase().trim().replaceAll("\\s+", "");
        }
        if (firstName != null) firstName = firstName.toLowerCase();
        if (lastName != null) lastName = lastName.toLowerCase();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        if (email != null) {
            email = email.toLowerCase().trim().replaceAll("\\s+", "");
        }
        if (firstName != null) firstName = firstName.toLowerCase();
        if (lastName != null) lastName = lastName.toLowerCase();
    }

}