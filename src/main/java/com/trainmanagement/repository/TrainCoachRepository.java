package com.trainmanagement.repository;

import com.trainmanagement.domain.TrainCoach;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrainCoach entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainCoachRepository extends JpaRepository<TrainCoach, Long> {}
