package com.trainmanagement.repository;

import com.trainmanagement.domain.Passenger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Passenger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {}
