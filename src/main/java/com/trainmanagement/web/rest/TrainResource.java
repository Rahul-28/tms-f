package com.trainmanagement.web.rest;

import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.service.TrainQueryService;
import com.trainmanagement.service.TrainService;
import com.trainmanagement.service.criteria.TrainCriteria;
import com.trainmanagement.service.dto.TrainDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.trainmanagement.domain.Train}.
 */
@RestController
@RequestMapping("/api/trains")
public class TrainResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainResource.class);

    private static final String ENTITY_NAME = "train";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainService trainService;

    private final TrainRepository trainRepository;

    private final TrainQueryService trainQueryService;

    public TrainResource(TrainService trainService, TrainRepository trainRepository, TrainQueryService trainQueryService) {
        this.trainService = trainService;
        this.trainRepository = trainRepository;
        this.trainQueryService = trainQueryService;
    }

    /**
     * {@code POST  /trains} : Create a new train.
     *
     * @param trainDTO the trainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainDTO, or with status {@code 400 (Bad Request)} if the train has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrainDTO> createTrain(@Valid @RequestBody TrainDTO trainDTO) throws URISyntaxException {
        LOG.debug("REST request to save Train : {}", trainDTO);
        if (trainDTO.getId() != null) {
            throw new BadRequestAlertException("A new train cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trainDTO = trainService.save(trainDTO);
        return ResponseEntity.created(new URI("/api/trains/" + trainDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, trainDTO.getId().toString()))
            .body(trainDTO);
    }

    /**
     * {@code PUT  /trains/:id} : Updates an existing train.
     *
     * @param id the id of the trainDTO to save.
     * @param trainDTO the trainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainDTO,
     * or with status {@code 400 (Bad Request)} if the trainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainDTO> updateTrain(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainDTO trainDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Train : {}, {}", id, trainDTO);
        if (trainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trainDTO = trainService.update(trainDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainDTO.getId().toString()))
            .body(trainDTO);
    }

    /**
     * {@code PATCH  /trains/:id} : Partial updates given fields of an existing train, field will ignore if it is null
     *
     * @param id the id of the trainDTO to save.
     * @param trainDTO the trainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainDTO,
     * or with status {@code 400 (Bad Request)} if the trainDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainDTO> partialUpdateTrain(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainDTO trainDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Train partially : {}, {}", id, trainDTO);
        if (trainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainDTO> result = trainService.partialUpdate(trainDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trains} : get all the trains.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trains in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrainDTO>> getAllTrains(
        TrainCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Trains by criteria: {}", criteria);

        Page<TrainDTO> page = trainQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trains/count} : count all the trains.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrains(TrainCriteria criteria) {
        LOG.debug("REST request to count Trains by criteria: {}", criteria);
        return ResponseEntity.ok().body(trainQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trains/:id} : get the "id" train.
     *
     * @param id the id of the trainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrain(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Train : {}", id);
        Optional<TrainDTO> trainDTO = trainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainDTO);
    }

    /**
     * {@code DELETE  /trains/:id} : delete the "id" train.
     *
     * @param id the id of the trainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Train : {}", id);
        trainService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
