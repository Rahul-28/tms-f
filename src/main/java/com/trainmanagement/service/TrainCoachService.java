package com.trainmanagement.service;

import com.trainmanagement.service.dto.TrainCoachDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.trainmanagement.domain.TrainCoach}.
 */
public interface TrainCoachService {
    /**
     * Save a trainCoach.
     *
     * @param trainCoachDTO the entity to save.
     * @return the persisted entity.
     */
    TrainCoachDTO save(TrainCoachDTO trainCoachDTO);

    /**
     * Updates a trainCoach.
     *
     * @param trainCoachDTO the entity to update.
     * @return the persisted entity.
     */
    TrainCoachDTO update(TrainCoachDTO trainCoachDTO);

    /**
     * Partially updates a trainCoach.
     *
     * @param trainCoachDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrainCoachDTO> partialUpdate(TrainCoachDTO trainCoachDTO);

    /**
     * Get all the trainCoaches.
     *
     * @return the list of entities.
     */
    List<TrainCoachDTO> findAll();

    /**
     * Get the "id" trainCoach.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrainCoachDTO> findOne(Long id);

    /**
     * Delete the "id" trainCoach.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
