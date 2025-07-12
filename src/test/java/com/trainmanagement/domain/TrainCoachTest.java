package com.trainmanagement.domain;

import static com.trainmanagement.domain.CoachTypeTestSamples.*;
import static com.trainmanagement.domain.TrainCoachTestSamples.*;
import static com.trainmanagement.domain.TrainTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainCoachTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainCoach.class);
        TrainCoach trainCoach1 = getTrainCoachSample1();
        TrainCoach trainCoach2 = new TrainCoach();
        assertThat(trainCoach1).isNotEqualTo(trainCoach2);

        trainCoach2.setId(trainCoach1.getId());
        assertThat(trainCoach1).isEqualTo(trainCoach2);

        trainCoach2 = getTrainCoachSample2();
        assertThat(trainCoach1).isNotEqualTo(trainCoach2);
    }

    @Test
    void coachTypeTest() {
        TrainCoach trainCoach = getTrainCoachRandomSampleGenerator();
        CoachType coachTypeBack = getCoachTypeRandomSampleGenerator();

        trainCoach.setCoachType(coachTypeBack);
        assertThat(trainCoach.getCoachType()).isEqualTo(coachTypeBack);

        trainCoach.coachType(null);
        assertThat(trainCoach.getCoachType()).isNull();
    }

    @Test
    void trainTest() {
        TrainCoach trainCoach = getTrainCoachRandomSampleGenerator();
        Train trainBack = getTrainRandomSampleGenerator();

        trainCoach.setTrain(trainBack);
        assertThat(trainCoach.getTrain()).isEqualTo(trainBack);

        trainCoach.train(null);
        assertThat(trainCoach.getTrain()).isNull();
    }
}
