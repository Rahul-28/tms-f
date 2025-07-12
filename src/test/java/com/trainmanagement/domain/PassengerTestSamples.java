package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PassengerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Passenger getPassengerSample1() {
        return new Passenger().id(1L).passengerName("passengerName1").age(1).coachNumber("coachNumber1").seatNumber("seatNumber1");
    }

    public static Passenger getPassengerSample2() {
        return new Passenger().id(2L).passengerName("passengerName2").age(2).coachNumber("coachNumber2").seatNumber("seatNumber2");
    }

    public static Passenger getPassengerRandomSampleGenerator() {
        return new Passenger()
            .id(longCount.incrementAndGet())
            .passengerName(UUID.randomUUID().toString())
            .age(intCount.incrementAndGet())
            .coachNumber(UUID.randomUUID().toString())
            .seatNumber(UUID.randomUUID().toString());
    }
}
