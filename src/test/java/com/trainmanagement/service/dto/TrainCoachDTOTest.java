package com.trainmanagement.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainCoachDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainCoachDTO.class);
        TrainCoachDTO trainCoachDTO1 = new TrainCoachDTO();
        trainCoachDTO1.setId(1L);
        TrainCoachDTO trainCoachDTO2 = new TrainCoachDTO();
        assertThat(trainCoachDTO1).isNotEqualTo(trainCoachDTO2);
        trainCoachDTO2.setId(trainCoachDTO1.getId());
        assertThat(trainCoachDTO1).isEqualTo(trainCoachDTO2);
        trainCoachDTO2.setId(2L);
        assertThat(trainCoachDTO1).isNotEqualTo(trainCoachDTO2);
        trainCoachDTO1.setId(null);
        assertThat(trainCoachDTO1).isNotEqualTo(trainCoachDTO2);
    }
}
