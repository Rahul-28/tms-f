package com.trainmanagement.web.rest;

import com.trainmanagement.repository.CoachTypeRepository;
import com.trainmanagement.service.CoachTypeService;
import com.trainmanagement.service.dto.CoachTypeDTO;
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
 * REST controller for managing {@link com.trainmanagement.domain.CoachType}.
 */
@RestController
@RequestMapping("/api/coach-types")
public class CoachTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CoachTypeResource.class);

    private static final String ENTITY_NAME = "coachType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoachTypeService coachTypeService;

    private final CoachTypeRepository coachTypeRepository;

    public CoachTypeResource(CoachTypeService coachTypeService, CoachTypeRepository coachTypeRepository) {
        this.coachTypeService = coachTypeService;
        this.coachTypeRepository = coachTypeRepository;
    }

    /**
     * {@code POST  /coach-types} : Create a new coachType.
     *
     * @param coachTypeDTO the coachTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coachTypeDTO, or with status {@code 400 (Bad Request)} if the coachType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CoachTypeDTO> createCoachType(@Valid @RequestBody CoachTypeDTO coachTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save CoachType : {}", coachTypeDTO);
        if (coachTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new coachType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coachTypeDTO = coachTypeService.save(coachTypeDTO);
        return ResponseEntity.created(new URI("/api/coach-types/" + coachTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coachTypeDTO.getId().toString()))
            .body(coachTypeDTO);
    }

    /**
     * {@code PUT  /coach-types/:id} : Updates an existing coachType.
     *
     * @param id the id of the coachTypeDTO to save.
     * @param coachTypeDTO the coachTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coachTypeDTO,
     * or with status {@code 400 (Bad Request)} if the coachTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coachTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoachTypeDTO> updateCoachType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoachTypeDTO coachTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CoachType : {}, {}", id, coachTypeDTO);
        if (coachTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coachTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coachTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coachTypeDTO = coachTypeService.update(coachTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coachTypeDTO.getId().toString()))
            .body(coachTypeDTO);
    }

    /**
     * {@code PATCH  /coach-types/:id} : Partial updates given fields of an existing coachType, field will ignore if it is null
     *
     * @param id the id of the coachTypeDTO to save.
     * @param coachTypeDTO the coachTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coachTypeDTO,
     * or with status {@code 400 (Bad Request)} if the coachTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coachTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coachTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoachTypeDTO> partialUpdateCoachType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoachTypeDTO coachTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CoachType partially : {}, {}", id, coachTypeDTO);
        if (coachTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coachTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coachTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoachTypeDTO> result = coachTypeService.partialUpdate(coachTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coachTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coach-types} : get all the coachTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coachTypes in body.
     */
    @GetMapping("")
    public List<CoachTypeDTO> getAllCoachTypes() {
        LOG.debug("REST request to get all CoachTypes");
        return coachTypeService.findAll();
    }

    /**
     * {@code GET  /coach-types/:id} : get the "id" coachType.
     *
     * @param id the id of the coachTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coachTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoachTypeDTO> getCoachType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CoachType : {}", id);
        Optional<CoachTypeDTO> coachTypeDTO = coachTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coachTypeDTO);
    }

    /**
     * {@code DELETE  /coach-types/:id} : delete the "id" coachType.
     *
     * @param id the id of the coachTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoachType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CoachType : {}", id);
        coachTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
