package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.Train;
import com.trainmanagement.service.dto.TrainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Train} and its DTO {@link TrainDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainMapper extends EntityMapper<TrainDTO, Train> {}
