package com.annztech.rewardsystem.modules.certificate.management.mapper;

import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.criteria.entity.Criterion;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.management.entity.Certificate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",  uses = StringsUtils.class)
public interface CertificateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "certificateCategory", ignore = true)
    @Mapping(target = "certificateTemplate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "criteriaRequired", ignore = true)
    Certificate toEntity(CertificateCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "certificate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Criterion toCriterionEntity(CertificateCreateDTO.CriterionCreateDTO dto);

    List<Criterion> toCriterionEntityList(List<CertificateCreateDTO.CriterionCreateDTO> dtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "certificateCategory", ignore = true)
    @Mapping(target = "certificateTemplate", ignore = true)
    void updateEntityFromDto(CertificateUpdateDTO dto, @MappingTarget Certificate certificate);

}
