package com.trainmanagement.service.mapper;

import static com.trainmanagement.domain.TrainCoachAsserts.*;
import static com.trainmanagement.domain.TrainCoachTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainCoachMapperTest {

    private TrainCoachMapper trainCoachMapper;

    @BeforeEach
    void setUp() {
        trainCoachMapper = new TrainCoachMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrainCoachSample1();
        var actual = trainCoachMapper.toEntity(trainCoachMapper.toDto(expected));
        assertTrainCoachAllPropertiesEquals(expected, actual);
    }
}
