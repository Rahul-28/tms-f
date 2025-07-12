package com.trainmanagement.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainDTO.class);
        TrainDTO trainDTO1 = new TrainDTO();
        trainDTO1.setId(1L);
        TrainDTO trainDTO2 = new TrainDTO();
        assertThat(trainDTO1).isNotEqualTo(trainDTO2);
        trainDTO2.setId(trainDTO1.getId());
        assertThat(trainDTO1).isEqualTo(trainDTO2);
        trainDTO2.setId(2L);
        assertThat(trainDTO1).isNotEqualTo(trainDTO2);
        trainDTO1.setId(null);
        assertThat(trainDTO1).isNotEqualTo(trainDTO2);
    }
}
