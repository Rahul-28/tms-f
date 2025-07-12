package com.trainmanagement.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BookingCriteriaTest {

    @Test
    void newBookingCriteriaHasAllFiltersNullTest() {
        var bookingCriteria = new BookingCriteria();
        assertThat(bookingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bookingCriteriaFluentMethodsCreatesFiltersTest() {
        var bookingCriteria = new BookingCriteria();

        setAllFilters(bookingCriteria);

        assertThat(bookingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bookingCriteriaCopyCreatesNullFilterTest() {
        var bookingCriteria = new BookingCriteria();
        var copy = bookingCriteria.copy();

        assertThat(bookingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bookingCriteria)
        );
    }

    @Test
    void bookingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bookingCriteria = new BookingCriteria();
        setAllFilters(bookingCriteria);

        var copy = bookingCriteria.copy();

        assertThat(bookingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bookingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bookingCriteria = new BookingCriteria();

        assertThat(bookingCriteria).hasToString("BookingCriteria{}");
    }

    private static void setAllFilters(BookingCriteria bookingCriteria) {
        bookingCriteria.id();
        bookingCriteria.pnrNumber();
        bookingCriteria.bookingDate();
        bookingCriteria.travellingDate();
        bookingCriteria.boardingStation();
        bookingCriteria.destinationStation();
        bookingCriteria.boardingTime();
        bookingCriteria.arrivalTime();
        bookingCriteria.totalFare();
        bookingCriteria.bookingStatus();
        bookingCriteria.modeOfPayment();
        bookingCriteria.additionalServices();
        bookingCriteria.coachNumber();
        bookingCriteria.seatNumber();
        bookingCriteria.passengersId();
        bookingCriteria.paymentId();
        bookingCriteria.customerId();
        bookingCriteria.trainId();
        bookingCriteria.distinct();
    }

    private static Condition<BookingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPnrNumber()) &&
                condition.apply(criteria.getBookingDate()) &&
                condition.apply(criteria.getTravellingDate()) &&
                condition.apply(criteria.getBoardingStation()) &&
                condition.apply(criteria.getDestinationStation()) &&
                condition.apply(criteria.getBoardingTime()) &&
                condition.apply(criteria.getArrivalTime()) &&
                condition.apply(criteria.getTotalFare()) &&
                condition.apply(criteria.getBookingStatus()) &&
                condition.apply(criteria.getModeOfPayment()) &&
                condition.apply(criteria.getAdditionalServices()) &&
                condition.apply(criteria.getCoachNumber()) &&
                condition.apply(criteria.getSeatNumber()) &&
                condition.apply(criteria.getPassengersId()) &&
                condition.apply(criteria.getPaymentId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getTrainId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BookingCriteria> copyFiltersAre(BookingCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPnrNumber(), copy.getPnrNumber()) &&
                condition.apply(criteria.getBookingDate(), copy.getBookingDate()) &&
                condition.apply(criteria.getTravellingDate(), copy.getTravellingDate()) &&
                condition.apply(criteria.getBoardingStation(), copy.getBoardingStation()) &&
                condition.apply(criteria.getDestinationStation(), copy.getDestinationStation()) &&
                condition.apply(criteria.getBoardingTime(), copy.getBoardingTime()) &&
                condition.apply(criteria.getArrivalTime(), copy.getArrivalTime()) &&
                condition.apply(criteria.getTotalFare(), copy.getTotalFare()) &&
                condition.apply(criteria.getBookingStatus(), copy.getBookingStatus()) &&
                condition.apply(criteria.getModeOfPayment(), copy.getModeOfPayment()) &&
                condition.apply(criteria.getAdditionalServices(), copy.getAdditionalServices()) &&
                condition.apply(criteria.getCoachNumber(), copy.getCoachNumber()) &&
                condition.apply(criteria.getSeatNumber(), copy.getSeatNumber()) &&
                condition.apply(criteria.getPassengersId(), copy.getPassengersId()) &&
                condition.apply(criteria.getPaymentId(), copy.getPaymentId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getTrainId(), copy.getTrainId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
