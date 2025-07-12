package com.trainmanagement.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrainCriteriaTest {

    @Test
    void newTrainCriteriaHasAllFiltersNullTest() {
        var trainCriteria = new TrainCriteria();
        assertThat(trainCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trainCriteriaFluentMethodsCreatesFiltersTest() {
        var trainCriteria = new TrainCriteria();

        setAllFilters(trainCriteria);

        assertThat(trainCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trainCriteriaCopyCreatesNullFilterTest() {
        var trainCriteria = new TrainCriteria();
        var copy = trainCriteria.copy();

        assertThat(trainCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trainCriteria)
        );
    }

    @Test
    void trainCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trainCriteria = new TrainCriteria();
        setAllFilters(trainCriteria);

        var copy = trainCriteria.copy();

        assertThat(trainCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trainCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trainCriteria = new TrainCriteria();

        assertThat(trainCriteria).hasToString("TrainCriteria{}");
    }

    private static void setAllFilters(TrainCriteria trainCriteria) {
        trainCriteria.id();
        trainCriteria.trainNumber();
        trainCriteria.trainName();
        trainCriteria.origin();
        trainCriteria.destination();
        trainCriteria.intermediateStop();
        trainCriteria.serviceStartDate();
        trainCriteria.serviceEndDate();
        trainCriteria.serviceType();
        trainCriteria.departureTime();
        trainCriteria.arrivalTime();
        trainCriteria.basicPrice();
        trainCriteria.isActive();
        trainCriteria.bookingsId();
        trainCriteria.trainCoachesId();
        trainCriteria.distinct();
    }

    private static Condition<TrainCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTrainNumber()) &&
                condition.apply(criteria.getTrainName()) &&
                condition.apply(criteria.getOrigin()) &&
                condition.apply(criteria.getDestination()) &&
                condition.apply(criteria.getIntermediateStop()) &&
                condition.apply(criteria.getServiceStartDate()) &&
                condition.apply(criteria.getServiceEndDate()) &&
                condition.apply(criteria.getServiceType()) &&
                condition.apply(criteria.getDepartureTime()) &&
                condition.apply(criteria.getArrivalTime()) &&
                condition.apply(criteria.getBasicPrice()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getBookingsId()) &&
                condition.apply(criteria.getTrainCoachesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrainCriteria> copyFiltersAre(TrainCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTrainNumber(), copy.getTrainNumber()) &&
                condition.apply(criteria.getTrainName(), copy.getTrainName()) &&
                condition.apply(criteria.getOrigin(), copy.getOrigin()) &&
                condition.apply(criteria.getDestination(), copy.getDestination()) &&
                condition.apply(criteria.getIntermediateStop(), copy.getIntermediateStop()) &&
                condition.apply(criteria.getServiceStartDate(), copy.getServiceStartDate()) &&
                condition.apply(criteria.getServiceEndDate(), copy.getServiceEndDate()) &&
                condition.apply(criteria.getServiceType(), copy.getServiceType()) &&
                condition.apply(criteria.getDepartureTime(), copy.getDepartureTime()) &&
                condition.apply(criteria.getArrivalTime(), copy.getArrivalTime()) &&
                condition.apply(criteria.getBasicPrice(), copy.getBasicPrice()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getBookingsId(), copy.getBookingsId()) &&
                condition.apply(criteria.getTrainCoachesId(), copy.getTrainCoachesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
