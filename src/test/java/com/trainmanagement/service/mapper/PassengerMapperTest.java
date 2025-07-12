package com.trainmanagement.service.mapper;

import static com.trainmanagement.domain.PassengerAsserts.*;
import static com.trainmanagement.domain.PassengerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PassengerMapperTest {

    private PassengerMapper passengerMapper;

    @BeforeEach
    void setUp() {
        passengerMapper = new PassengerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPassengerSample1();
        var actual = passengerMapper.toEntity(passengerMapper.toDto(expected));
        assertPassengerAllPropertiesEquals(expected, actual);
    }
}
