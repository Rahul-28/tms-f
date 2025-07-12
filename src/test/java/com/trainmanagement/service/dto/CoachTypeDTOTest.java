package com.trainmanagement.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoachTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoachTypeDTO.class);
        CoachTypeDTO coachTypeDTO1 = new CoachTypeDTO();
        coachTypeDTO1.setId(1L);
        CoachTypeDTO coachTypeDTO2 = new CoachTypeDTO();
        assertThat(coachTypeDTO1).isNotEqualTo(coachTypeDTO2);
        coachTypeDTO2.setId(coachTypeDTO1.getId());
        assertThat(coachTypeDTO1).isEqualTo(coachTypeDTO2);
        coachTypeDTO2.setId(2L);
        assertThat(coachTypeDTO1).isNotEqualTo(coachTypeDTO2);
        coachTypeDTO1.setId(null);
        assertThat(coachTypeDTO1).isNotEqualTo(coachTypeDTO2);
    }
}
