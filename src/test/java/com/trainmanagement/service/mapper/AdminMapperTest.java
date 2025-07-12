package com.trainmanagement.service.mapper;

import static com.trainmanagement.domain.AdminAsserts.*;
import static com.trainmanagement.domain.AdminTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminMapperTest {

    private AdminMapper adminMapper;

    @BeforeEach
    void setUp() {
        adminMapper = new AdminMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdminSample1();
        var actual = adminMapper.toEntity(adminMapper.toDto(expected));
        assertAdminAllPropertiesEquals(expected, actual);
    }
}
