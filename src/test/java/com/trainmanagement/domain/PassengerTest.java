package com.trainmanagement.domain;

import static com.trainmanagement.domain.BookingTestSamples.*;
import static com.trainmanagement.domain.PassengerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PassengerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Passenger.class);
        Passenger passenger1 = getPassengerSample1();
        Passenger passenger2 = new Passenger();
        assertThat(passenger1).isNotEqualTo(passenger2);

        passenger2.setId(passenger1.getId());
        assertThat(passenger1).isEqualTo(passenger2);

        passenger2 = getPassengerSample2();
        assertThat(passenger1).isNotEqualTo(passenger2);
    }

    @Test
    void bookingTest() {
        Passenger passenger = getPassengerRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        passenger.setBooking(bookingBack);
        assertThat(passenger.getBooking()).isEqualTo(bookingBack);

        passenger.booking(null);
        assertThat(passenger.getBooking()).isNull();
    }
}
