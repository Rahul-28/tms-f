package com.trainmanagement.repository;

import com.trainmanagement.domain.CoachType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CoachType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoachTypeRepository extends JpaRepository<CoachType, Long> {}
