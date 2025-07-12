package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment()
            .id(1L)
            .paymentId("paymentId1")
            .transactionId("transactionId1")
            .receiptNumber("receiptNumber1")
            .cardNumber("cardNumber1")
            .expiryDate("expiryDate1")
            .cvv("cvv1")
            .cardholderName("cardholderName1");
    }

    public static Payment getPaymentSample2() {
        return new Payment()
            .id(2L)
            .paymentId("paymentId2")
            .transactionId("transactionId2")
            .receiptNumber("receiptNumber2")
            .cardNumber("cardNumber2")
            .expiryDate("expiryDate2")
            .cvv("cvv2")
            .cardholderName("cardholderName2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .paymentId(UUID.randomUUID().toString())
            .transactionId(UUID.randomUUID().toString())
            .receiptNumber(UUID.randomUUID().toString())
            .cardNumber(UUID.randomUUID().toString())
            .expiryDate(UUID.randomUUID().toString())
            .cvv(UUID.randomUUID().toString())
            .cardholderName(UUID.randomUUID().toString());
    }
}
