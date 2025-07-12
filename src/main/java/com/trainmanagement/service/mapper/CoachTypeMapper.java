package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.CoachType;
import com.trainmanagement.service.dto.CoachTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoachType} and its DTO {@link CoachTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoachTypeMapper extends EntityMapper<CoachTypeDTO, CoachType> {}
