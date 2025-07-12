package com.trainmanagement.domain;

import static com.trainmanagement.domain.BookingTestSamples.*;
import static com.trainmanagement.domain.TrainCoachTestSamples.*;
import static com.trainmanagement.domain.TrainTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Train.class);
        Train train1 = getTrainSample1();
        Train train2 = new Train();
        assertThat(train1).isNotEqualTo(train2);

        train2.setId(train1.getId());
        assertThat(train1).isEqualTo(train2);

        train2 = getTrainSample2();
        assertThat(train1).isNotEqualTo(train2);
    }

    @Test
    void bookingsTest() {
        Train train = getTrainRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        train.addBookings(bookingBack);
        assertThat(train.getBookings()).containsOnly(bookingBack);
        assertThat(bookingBack.getTrain()).isEqualTo(train);

        train.removeBookings(bookingBack);
        assertThat(train.getBookings()).doesNotContain(bookingBack);
        assertThat(bookingBack.getTrain()).isNull();

        train.bookings(new HashSet<>(Set.of(bookingBack)));
        assertThat(train.getBookings()).containsOnly(bookingBack);
        assertThat(bookingBack.getTrain()).isEqualTo(train);

        train.setBookings(new HashSet<>());
        assertThat(train.getBookings()).doesNotContain(bookingBack);
        assertThat(bookingBack.getTrain()).isNull();
    }

    @Test
    void trainCoachesTest() {
        Train train = getTrainRandomSampleGenerator();
        TrainCoach trainCoachBack = getTrainCoachRandomSampleGenerator();

        train.addTrainCoaches(trainCoachBack);
        assertThat(train.getTrainCoaches()).containsOnly(trainCoachBack);
        assertThat(trainCoachBack.getTrain()).isEqualTo(train);

        train.removeTrainCoaches(trainCoachBack);
        assertThat(train.getTrainCoaches()).doesNotContain(trainCoachBack);
        assertThat(trainCoachBack.getTrain()).isNull();

        train.trainCoaches(new HashSet<>(Set.of(trainCoachBack)));
        assertThat(train.getTrainCoaches()).containsOnly(trainCoachBack);
        assertThat(trainCoachBack.getTrain()).isEqualTo(train);

        train.setTrainCoaches(new HashSet<>());
        assertThat(train.getTrainCoaches()).doesNotContain(trainCoachBack);
        assertThat(trainCoachBack.getTrain()).isNull();
    }
}
