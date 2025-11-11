package com.annztech.rewardsystem.modules.certificate.category.mapper;

import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryCreateDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.category.entity.CertificateCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring",  uses = StringsUtils.class)
public interface CertificateCategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CertificateCategory toEntity (CertificateCategoryCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "nameEnglish", conditionQualifiedByName = "isNotBlank")
    @Mapping(target = "nameArabic", conditionQualifiedByName = "isNotBlank")
    void updateEntityFromDto(CertificateCategoryUpdateDTO dto, @MappingTarget CertificateCategory certificateCategory);
}
