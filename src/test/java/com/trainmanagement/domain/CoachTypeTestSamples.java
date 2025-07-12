package com.trainmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CoachTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CoachType getCoachTypeSample1() {
        return new CoachType().id(1L).coachId("coachId1").coachName("coachName1").seatCapacity(1);
    }

    public static CoachType getCoachTypeSample2() {
        return new CoachType().id(2L).coachId("coachId2").coachName("coachName2").seatCapacity(2);
    }

    public static CoachType getCoachTypeRandomSampleGenerator() {
        return new CoachType()
            .id(longCount.incrementAndGet())
            .coachId(UUID.randomUUID().toString())
            .coachName(UUID.randomUUID().toString())
            .seatCapacity(intCount.incrementAndGet());
    }
}
