package com.trainmanagement.service.mapper;

import static com.trainmanagement.domain.CoachTypeAsserts.*;
import static com.trainmanagement.domain.CoachTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoachTypeMapperTest {

    private CoachTypeMapper coachTypeMapper;

    @BeforeEach
    void setUp() {
        coachTypeMapper = new CoachTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCoachTypeSample1();
        var actual = coachTypeMapper.toEntity(coachTypeMapper.toDto(expected));
        assertCoachTypeAllPropertiesEquals(expected, actual);
    }
}
