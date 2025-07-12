package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrainCoachTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrainCoach getTrainCoachSample1() {
        return new TrainCoach().id(1L).trainNumber("trainNumber1").seatCapacity(1).availableSeats(1);
    }

    public static TrainCoach getTrainCoachSample2() {
        return new TrainCoach().id(2L).trainNumber("trainNumber2").seatCapacity(2).availableSeats(2);
    }

    public static TrainCoach getTrainCoachRandomSampleGenerator() {
        return new TrainCoach()
            .id(longCount.incrementAndGet())
            .trainNumber(UUID.randomUUID().toString())
            .seatCapacity(intCount.incrementAndGet())
            .availableSeats(intCount.incrementAndGet());
    }
}
