package com.trainmanagement.service.mapper;

import com.trainmanagement.domain.CoachType;
import com.trainmanagement.domain.Train;
import com.trainmanagement.domain.TrainCoach;
import com.trainmanagement.service.dto.CoachTypeDTO;
import com.trainmanagement.service.dto.TrainCoachDTO;
import com.trainmanagement.service.dto.TrainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrainCoach} and its DTO {@link TrainCoachDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainCoachMapper extends EntityMapper<TrainCoachDTO, TrainCoach> {
    @Mapping(target = "coachType", source = "coachType", qualifiedByName = "coachTypeId")
    @Mapping(target = "train", source = "train", qualifiedByName = "trainId")
    TrainCoachDTO toDto(TrainCoach s);

    @Named("coachTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CoachTypeDTO toDtoCoachTypeId(CoachType coachType);

    @Named("trainId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrainDTO toDtoTrainId(Train train);
}
