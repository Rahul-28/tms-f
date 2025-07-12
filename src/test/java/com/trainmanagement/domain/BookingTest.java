package com.trainmanagement.domain;

import static com.trainmanagement.domain.BookingTestSamples.*;
import static com.trainmanagement.domain.CustomerTestSamples.*;
import static com.trainmanagement.domain.PassengerTestSamples.*;
import static com.trainmanagement.domain.PaymentTestSamples.*;
import static com.trainmanagement.domain.TrainTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = getBookingSample1();
        Booking booking2 = new Booking();
        assertThat(booking1).isNotEqualTo(booking2);

        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);

        booking2 = getBookingSample2();
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    void passengersTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Passenger passengerBack = getPassengerRandomSampleGenerator();

        booking.addPassengers(passengerBack);
        assertThat(booking.getPassengers()).containsOnly(passengerBack);
        assertThat(passengerBack.getBooking()).isEqualTo(booking);

        booking.removePassengers(passengerBack);
        assertThat(booking.getPassengers()).doesNotContain(passengerBack);
        assertThat(passengerBack.getBooking()).isNull();

        booking.passengers(new HashSet<>(Set.of(passengerBack)));
        assertThat(booking.getPassengers()).containsOnly(passengerBack);
        assertThat(passengerBack.getBooking()).isEqualTo(booking);

        booking.setPassengers(new HashSet<>());
        assertThat(booking.getPassengers()).doesNotContain(passengerBack);
        assertThat(passengerBack.getBooking()).isNull();
    }

    @Test
    void paymentTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        booking.addPayment(paymentBack);
        assertThat(booking.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBooking()).isEqualTo(booking);

        booking.removePayment(paymentBack);
        assertThat(booking.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBooking()).isNull();

        booking.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(booking.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBooking()).isEqualTo(booking);

        booking.setPayments(new HashSet<>());
        assertThat(booking.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBooking()).isNull();
    }

    @Test
    void customerTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        booking.setCustomer(customerBack);
        assertThat(booking.getCustomer()).isEqualTo(customerBack);

        booking.customer(null);
        assertThat(booking.getCustomer()).isNull();
    }

    @Test
    void trainTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Train trainBack = getTrainRandomSampleGenerator();

        booking.setTrain(trainBack);
        assertThat(booking.getTrain()).isEqualTo(trainBack);

        booking.train(null);
        assertThat(booking.getTrain()).isNull();
    }
}
