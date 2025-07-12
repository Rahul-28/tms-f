package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .customerId("customerId1")
            .username("username1")
            .password("password1")
            .email("email1")
            .mobileNumber("mobileNumber1")
            .aadhaarNumber("aadhaarNumber1")
            .address("address1")
            .contactInformation("contactInformation1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .customerId("customerId2")
            .username("username2")
            .password("password2")
            .email("email2")
            .mobileNumber("mobileNumber2")
            .aadhaarNumber("aadhaarNumber2")
            .address("address2")
            .contactInformation("contactInformation2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .customerId(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .mobileNumber(UUID.randomUUID().toString())
            .aadhaarNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .contactInformation(UUID.randomUUID().toString());
    }
}
