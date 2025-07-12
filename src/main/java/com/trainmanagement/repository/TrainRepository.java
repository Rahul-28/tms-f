package com.trainmanagement.repository;

import com.trainmanagement.domain.Train;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Train entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainRepository extends JpaRepository<Train, Long>, JpaSpecificationExecutor<Train> {}
