package com.trainmanagement.domain;

import static com.trainmanagement.domain.BookingTestSamples.*;
import static com.trainmanagement.domain.InvoiceTestSamples.*;
import static com.trainmanagement.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void invoiceTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        payment.addInvoice(invoiceBack);
        assertThat(payment.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getPayment()).isEqualTo(payment);

        payment.removeInvoice(invoiceBack);
        assertThat(payment.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getPayment()).isNull();

        payment.invoices(new HashSet<>(Set.of(invoiceBack)));
        assertThat(payment.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getPayment()).isEqualTo(payment);

        payment.setInvoices(new HashSet<>());
        assertThat(payment.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getPayment()).isNull();
    }

    @Test
    void bookingTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        payment.setBooking(bookingBack);
        assertThat(payment.getBooking()).isEqualTo(bookingBack);

        payment.booking(null);
        assertThat(payment.getBooking()).isNull();
    }
}
