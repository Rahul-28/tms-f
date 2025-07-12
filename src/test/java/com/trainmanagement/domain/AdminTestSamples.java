package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdminTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Admin getAdminSample1() {
        return new Admin().id(1L).adminId("adminId1").username("username1").password("password1").email("email1");
    }

    public static Admin getAdminSample2() {
        return new Admin().id(2L).adminId("adminId2").username("username2").password("password2").email("email2");
    }

    public static Admin getAdminRandomSampleGenerator() {
        return new Admin()
            .id(longCount.incrementAndGet())
            .adminId(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
