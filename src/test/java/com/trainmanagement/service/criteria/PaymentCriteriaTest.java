package com.trainmanagement.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentCriteriaTest {

    @Test
    void newPaymentCriteriaHasAllFiltersNullTest() {
        var paymentCriteria = new PaymentCriteria();
        assertThat(paymentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentCriteria = new PaymentCriteria();

        setAllFilters(paymentCriteria);

        assertThat(paymentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentCriteriaCopyCreatesNullFilterTest() {
        var paymentCriteria = new PaymentCriteria();
        var copy = paymentCriteria.copy();

        assertThat(paymentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentCriteria)
        );
    }

    @Test
    void paymentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentCriteria = new PaymentCriteria();
        setAllFilters(paymentCriteria);

        var copy = paymentCriteria.copy();

        assertThat(paymentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentCriteria = new PaymentCriteria();

        assertThat(paymentCriteria).hasToString("PaymentCriteria{}");
    }

    private static void setAllFilters(PaymentCriteria paymentCriteria) {
        paymentCriteria.id();
        paymentCriteria.paymentId();
        paymentCriteria.transactionId();
        paymentCriteria.receiptNumber();
        paymentCriteria.transactionDate();
        paymentCriteria.transactionType();
        paymentCriteria.transactionAmount();
        paymentCriteria.transactionStatus();
        paymentCriteria.cardNumber();
        paymentCriteria.expiryDate();
        paymentCriteria.cvv();
        paymentCriteria.cardholderName();
        paymentCriteria.invoiceId();
        paymentCriteria.bookingId();
        paymentCriteria.distinct();
    }

    private static Condition<PaymentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPaymentId()) &&
                condition.apply(criteria.getTransactionId()) &&
                condition.apply(criteria.getReceiptNumber()) &&
                condition.apply(criteria.getTransactionDate()) &&
                condition.apply(criteria.getTransactionType()) &&
                condition.apply(criteria.getTransactionAmount()) &&
                condition.apply(criteria.getTransactionStatus()) &&
                condition.apply(criteria.getCardNumber()) &&
                condition.apply(criteria.getExpiryDate()) &&
                condition.apply(criteria.getCvv()) &&
                condition.apply(criteria.getCardholderName()) &&
                condition.apply(criteria.getInvoiceId()) &&
                condition.apply(criteria.getBookingId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentCriteria> copyFiltersAre(PaymentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPaymentId(), copy.getPaymentId()) &&
                condition.apply(criteria.getTransactionId(), copy.getTransactionId()) &&
                condition.apply(criteria.getReceiptNumber(), copy.getReceiptNumber()) &&
                condition.apply(criteria.getTransactionDate(), copy.getTransactionDate()) &&
                condition.apply(criteria.getTransactionType(), copy.getTransactionType()) &&
                condition.apply(criteria.getTransactionAmount(), copy.getTransactionAmount()) &&
                condition.apply(criteria.getTransactionStatus(), copy.getTransactionStatus()) &&
                condition.apply(criteria.getCardNumber(), copy.getCardNumber()) &&
                condition.apply(criteria.getExpiryDate(), copy.getExpiryDate()) &&
                condition.apply(criteria.getCvv(), copy.getCvv()) &&
                condition.apply(criteria.getCardholderName(), copy.getCardholderName()) &&
                condition.apply(criteria.getInvoiceId(), copy.getInvoiceId()) &&
                condition.apply(criteria.getBookingId(), copy.getBookingId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
