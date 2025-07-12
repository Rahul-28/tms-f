package com.trainmanagement.service;

import com.trainmanagement.service.dto.TrainDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.trainmanagement.domain.Train}.
 */
public interface TrainService {
    /**
     * Save a train.
     *
     * @param trainDTO the entity to save.
     * @return the persisted entity.
     */
    TrainDTO save(TrainDTO trainDTO);

    /**
     * Updates a train.
     *
     * @param trainDTO the entity to update.
     * @return the persisted entity.
     */
    TrainDTO update(TrainDTO trainDTO);

    /**
     * Partially updates a train.
     *
     * @param trainDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrainDTO> partialUpdate(TrainDTO trainDTO);

    /**
     * Get the "id" train.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrainDTO> findOne(Long id);

    /**
     * Delete the "id" train.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
