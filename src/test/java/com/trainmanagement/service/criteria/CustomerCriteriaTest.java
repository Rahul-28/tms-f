package com.trainmanagement.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerCriteriaTest {

    @Test
    void newCustomerCriteriaHasAllFiltersNullTest() {
        var customerCriteria = new CustomerCriteria();
        assertThat(customerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void customerCriteriaFluentMethodsCreatesFiltersTest() {
        var customerCriteria = new CustomerCriteria();

        setAllFilters(customerCriteria);

        assertThat(customerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void customerCriteriaCopyCreatesNullFilterTest() {
        var customerCriteria = new CustomerCriteria();
        var copy = customerCriteria.copy();

        assertThat(customerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(customerCriteria)
        );
    }

    @Test
    void customerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerCriteria = new CustomerCriteria();
        setAllFilters(customerCriteria);

        var copy = customerCriteria.copy();

        assertThat(customerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(customerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerCriteria = new CustomerCriteria();

        assertThat(customerCriteria).hasToString("CustomerCriteria{}");
    }

    private static void setAllFilters(CustomerCriteria customerCriteria) {
        customerCriteria.id();
        customerCriteria.customerId();
        customerCriteria.username();
        customerCriteria.password();
        customerCriteria.email();
        customerCriteria.mobileNumber();
        customerCriteria.aadhaarNumber();
        customerCriteria.address();
        customerCriteria.contactInformation();
        customerCriteria.registrationDate();
        customerCriteria.isActive();
        customerCriteria.bookingsId();
        customerCriteria.distinct();
    }

    private static Condition<CustomerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getPassword()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getMobileNumber()) &&
                condition.apply(criteria.getAadhaarNumber()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getContactInformation()) &&
                condition.apply(criteria.getRegistrationDate()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getBookingsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustomerCriteria> copyFiltersAre(CustomerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getPassword(), copy.getPassword()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getMobileNumber(), copy.getMobileNumber()) &&
                condition.apply(criteria.getAadhaarNumber(), copy.getAadhaarNumber()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getContactInformation(), copy.getContactInformation()) &&
                condition.apply(criteria.getRegistrationDate(), copy.getRegistrationDate()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getBookingsId(), copy.getBookingsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
