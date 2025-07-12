package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrainTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Train getTrainSample1() {
        return new Train()
            .id(1L)
            .trainNumber("trainNumber1")
            .trainName("trainName1")
            .origin("origin1")
            .destination("destination1")
            .intermediateStop("intermediateStop1");
    }

    public static Train getTrainSample2() {
        return new Train()
            .id(2L)
            .trainNumber("trainNumber2")
            .trainName("trainName2")
            .origin("origin2")
            .destination("destination2")
            .intermediateStop("intermediateStop2");
    }

    public static Train getTrainRandomSampleGenerator() {
        return new Train()
            .id(longCount.incrementAndGet())
            .trainNumber(UUID.randomUUID().toString())
            .trainName(UUID.randomUUID().toString())
            .origin(UUID.randomUUID().toString())
            .destination(UUID.randomUUID().toString())
            .intermediateStop(UUID.randomUUID().toString());
    }
}
