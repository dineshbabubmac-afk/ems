package com.annztech.rewardsystem.modules.certificate.template.mapper;

import com.annztech.rewardsystem.common.utils.StringsUtils;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.template.entity.Template;
import org.mapstruct.*;

@Mapper(componentModel = "spring",  uses = StringsUtils.class)
public interface TemplateMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Template toEntity(TemplateCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "name", conditionQualifiedByName = "isNotBlank")
    @Mapping(target = "path", conditionQualifiedByName = "isNotBlank")
    @Mapping(target = "description", conditionQualifiedByName = "isNotBlank")
    void updateEntityFromDto(TemplateUpdateDTO dto, @MappingTarget Template template);
}
