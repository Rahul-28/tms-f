package com.trainmanagement.service;

import com.trainmanagement.service.dto.CoachTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.trainmanagement.domain.CoachType}.
 */
public interface CoachTypeService {
    /**
     * Save a coachType.
     *
     * @param coachTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CoachTypeDTO save(CoachTypeDTO coachTypeDTO);

    /**
     * Updates a coachType.
     *
     * @param coachTypeDTO the entity to update.
     * @return the persisted entity.
     */
    CoachTypeDTO update(CoachTypeDTO coachTypeDTO);

    /**
     * Partially updates a coachType.
     *
     * @param coachTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoachTypeDTO> partialUpdate(CoachTypeDTO coachTypeDTO);

    /**
     * Get all the coachTypes.
     *
     * @return the list of entities.
     */
    List<CoachTypeDTO> findAll();

    /**
     * Get the "id" coachType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoachTypeDTO> findOne(Long id);

    /**
     * Delete the "id" coachType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
