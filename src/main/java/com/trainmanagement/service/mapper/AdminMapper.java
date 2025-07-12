package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.Admin;
import com.trainmanagement.service.dto.AdminDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Admin} and its DTO {@link AdminDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminMapper extends EntityMapper<AdminDTO, Admin> {}
