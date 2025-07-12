package com.trainmanagement.service.mapper;

import static com.trainmanagement.domain.TrainAsserts.*;
import static com.trainmanagement.domain.TrainTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainMapperTest {

    private TrainMapper trainMapper;

    @BeforeEach
    void setUp() {
        trainMapper = new TrainMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrainSample1();
        var actual = trainMapper.toEntity(trainMapper.toDto(expected));
        assertTrainAllPropertiesEquals(expected, actual);
    }
}
