package com.trainmanagement.domain;

import static com.trainmanagement.domain.CoachTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoachTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoachType.class);
        CoachType coachType1 = getCoachTypeSample1();
        CoachType coachType2 = new CoachType();
        assertThat(coachType1).isNotEqualTo(coachType2);

        coachType2.setId(coachType1.getId());
        assertThat(coachType1).isEqualTo(coachType2);

        coachType2 = getCoachTypeSample2();
        assertThat(coachType1).isNotEqualTo(coachType2);
    }
}
