package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Booking getBookingSample1() {
        return new Booking()
            .id(1L)
            .pnrNumber("pnrNumber1")
            .boardingStation("boardingStation1")
            .destinationStation("destinationStation1")
            .coachNumber("coachNumber1")
            .seatNumber("seatNumber1");
    }

    public static Booking getBookingSample2() {
        return new Booking()
            .id(2L)
            .pnrNumber("pnrNumber2")
            .boardingStation("boardingStation2")
            .destinationStation("destinationStation2")
            .coachNumber("coachNumber2")
            .seatNumber("seatNumber2");
    }

    public static Booking getBookingRandomSampleGenerator() {
        return new Booking()
            .id(longCount.incrementAndGet())
            .pnrNumber(UUID.randomUUID().toString())
            .boardingStation(UUID.randomUUID().toString())
            .destinationStation(UUID.randomUUID().toString())
            .coachNumber(UUID.randomUUID().toString())
            .seatNumber(UUID.randomUUID().toString());
    }
}
