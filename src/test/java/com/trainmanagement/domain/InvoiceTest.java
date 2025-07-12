package com.trainmanagement.domain;

import static com.trainmanagement.domain.InvoiceTestSamples.*;
import static com.trainmanagement.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.trainmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void paymentTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        invoice.setPayment(paymentBack);
        assertThat(invoice.getPayment()).isEqualTo(paymentBack);

        invoice.payment(null);
        assertThat(invoice.getPayment()).isNull();
    }
}
