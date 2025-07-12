package com.trainmanagement.web.rest;

import com.trainmanagement.repository.TrainCoachRepository;
import com.trainmanagement.service.TrainCoachService;
import com.trainmanagement.service.dto.TrainCoachDTO;
import com.trainmanagement.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.trainmanagement.domain.TrainCoach}.
 */
@RestController
@RequestMapping("/api/train-coaches")
public class TrainCoachResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainCoachResource.class);

    private static final String ENTITY_NAME = "trainCoach";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainCoachService trainCoachService;

    private final TrainCoachRepository trainCoachRepository;

    public TrainCoachResource(TrainCoachService trainCoachService, TrainCoachRepository trainCoachRepository) {
        this.trainCoachService = trainCoachService;
        this.trainCoachRepository = trainCoachRepository;
    }

    /**
     * {@code POST  /train-coaches} : Create a new trainCoach.
     *
     * @param trainCoachDTO the trainCoachDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainCoachDTO, or with status {@code 400 (Bad Request)} if the trainCoach has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrainCoachDTO> createTrainCoach(@Valid @RequestBody TrainCoachDTO trainCoachDTO) throws URISyntaxException {
        LOG.debug("REST request to save TrainCoach : {}", trainCoachDTO);
        if (trainCoachDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainCoach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trainCoachDTO = trainCoachService.save(trainCoachDTO);
        return ResponseEntity.created(new URI("/api/train-coaches/" + trainCoachDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, trainCoachDTO.getId().toString()))
            .body(trainCoachDTO);
    }

    /**
     * {@code PUT  /train-coaches/:id} : Updates an existing trainCoach.
     *
     * @param id the id of the trainCoachDTO to save.
     * @param trainCoachDTO the trainCoachDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainCoachDTO,
     * or with status {@code 400 (Bad Request)} if the trainCoachDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainCoachDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainCoachDTO> updateTrainCoach(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainCoachDTO trainCoachDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrainCoach : {}, {}", id, trainCoachDTO);
        if (trainCoachDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainCoachDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainCoachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trainCoachDTO = trainCoachService.update(trainCoachDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainCoachDTO.getId().toString()))
            .body(trainCoachDTO);
    }

    /**
     * {@code PATCH  /train-coaches/:id} : Partial updates given fields of an existing trainCoach, field will ignore if it is null
     *
     * @param id the id of the trainCoachDTO to save.
     * @param trainCoachDTO the trainCoachDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainCoachDTO,
     * or with status {@code 400 (Bad Request)} if the trainCoachDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainCoachDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainCoachDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainCoachDTO> partialUpdateTrainCoach(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainCoachDTO trainCoachDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrainCoach partially : {}, {}", id, trainCoachDTO);
        if (trainCoachDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainCoachDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainCoachRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainCoachDTO> result = trainCoachService.partialUpdate(trainCoachDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainCoachDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /train-coaches} : get all the trainCoaches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainCoaches in body.
     */
    @GetMapping("")
    public List<TrainCoachDTO> getAllTrainCoaches() {
        LOG.debug("REST request to get all TrainCoaches");
        return trainCoachService.findAll();
    }

    /**
     * {@code GET  /train-coaches/:id} : get the "id" trainCoach.
     *
     * @param id the id of the trainCoachDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainCoachDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainCoachDTO> getTrainCoach(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrainCoach : {}", id);
        Optional<TrainCoachDTO> trainCoachDTO = trainCoachService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainCoachDTO);
    }

    /**
     * {@code DELETE  /train-coaches/:id} : delete the "id" trainCoach.
     *
     * @param id the id of the trainCoachDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainCoach(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrainCoach : {}", id);
        trainCoachService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
