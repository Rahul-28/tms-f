package com.trainmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PassengerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassengerAllPropertiesEquals(Passenger expected, Passenger actual) {
        assertPassengerAutoGeneratedPropertiesEquals(expected, actual);
        assertPassengerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassengerAllUpdatablePropertiesEquals(Passenger expected, Passenger actual) {
        assertPassengerUpdatableFieldsEquals(expected, actual);
        assertPassengerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassengerAutoGeneratedPropertiesEquals(Passenger expected, Passenger actual) {
        assertThat(actual)
            .as("Verify Passenger auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassengerUpdatableFieldsEquals(Passenger expected, Passenger actual) {
        assertThat(actual)
            .as("Verify Passenger relevant properties")
            .satisfies(a -> assertThat(a.getPassengerName()).as("check passengerName").isEqualTo(expected.getPassengerName()))
            .satisfies(a -> assertThat(a.getAge()).as("check age").isEqualTo(expected.getAge()))
            .satisfies(a -> assertThat(a.getCoachNumber()).as("check coachNumber").isEqualTo(expected.getCoachNumber()))
            .satisfies(a -> assertThat(a.getSeatNumber()).as("check seatNumber").isEqualTo(expected.getSeatNumber()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassengerUpdatableRelationshipsEquals(Passenger expected, Passenger actual) {
        assertThat(actual)
            .as("Verify Passenger relationships")
            .satisfies(a -> assertThat(a.getBooking()).as("check booking").isEqualTo(expected.getBooking()));
    }
}
