package com.annztech.rewardsystem.modules.lookups.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="app_type_lookup")
public class AppTypeLookUp {
    @Id
    @Column(name="code", nullable = false)
    private String code;
    @Column(name="name_en", nullable = false)
    private String nameEn;
    @Column(name="name_ar", nullable = false)
    private String nameAr;
}
