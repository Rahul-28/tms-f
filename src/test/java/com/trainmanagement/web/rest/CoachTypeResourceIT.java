package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.CoachTypeAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trainmanagement.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.CoachType;
import com.trainmanagement.repository.CoachTypeRepository;
import com.trainmanagement.service.dto.CoachTypeDTO;
import com.trainmanagement.service.mapper.CoachTypeMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CoachTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoachTypeResourceIT {

    private static final String DEFAULT_COACH_ID = "AAAAAAAAAA";
    private static final String UPDATED_COACH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COACH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COACH_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEAT_CAPACITY = 1;
    private static final Integer UPDATED_SEAT_CAPACITY = 2;

    private static final BigDecimal DEFAULT_FARE_MULTIPLIER = new BigDecimal(1);
    private static final BigDecimal UPDATED_FARE_MULTIPLIER = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/coach-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoachTypeRepository coachTypeRepository;

    @Autowired
    private CoachTypeMapper coachTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoachTypeMockMvc;

    private CoachType coachType;

    private CoachType insertedCoachType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoachType createEntity() {
        return new CoachType()
            .coachId(DEFAULT_COACH_ID)
            .coachName(DEFAULT_COACH_NAME)
            .seatCapacity(DEFAULT_SEAT_CAPACITY)
            .fareMultiplier(DEFAULT_FARE_MULTIPLIER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoachType createUpdatedEntity() {
        return new CoachType()
            .coachId(UPDATED_COACH_ID)
            .coachName(UPDATED_COACH_NAME)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .fareMultiplier(UPDATED_FARE_MULTIPLIER);
    }

    @BeforeEach
    void initTest() {
        coachType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCoachType != null) {
            coachTypeRepository.delete(insertedCoachType);
            insertedCoachType = null;
        }
    }

    @Test
    @Transactional
    void createCoachType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);
        var returnedCoachTypeDTO = om.readValue(
            restCoachTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoachTypeDTO.class
        );

        // Validate the CoachType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCoachType = coachTypeMapper.toEntity(returnedCoachTypeDTO);
        assertCoachTypeUpdatableFieldsEquals(returnedCoachType, getPersistedCoachType(returnedCoachType));

        insertedCoachType = returnedCoachType;
    }

    @Test
    @Transactional
    void createCoachTypeWithExistingId() throws Exception {
        // Create the CoachType with an existing ID
        coachType.setId(1L);
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoachTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCoachIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachType.setCoachId(null);

        // Create the CoachType, which fails.
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        restCoachTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoachNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachType.setCoachName(null);

        // Create the CoachType, which fails.
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        restCoachTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeatCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachType.setSeatCapacity(null);

        // Create the CoachType, which fails.
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        restCoachTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFareMultiplierIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coachType.setFareMultiplier(null);

        // Create the CoachType, which fails.
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        restCoachTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoachTypes() throws Exception {
        // Initialize the database
        insertedCoachType = coachTypeRepository.saveAndFlush(coachType);

        // Get all the coachTypeList
        restCoachTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coachType.getId().intValue())))
            .andExpect(jsonPath("$.[*].coachId").value(hasItem(DEFAULT_COACH_ID)))
            .andExpect(jsonPath("$.[*].coachName").value(hasItem(DEFAULT_COACH_NAME)))
            .andExpect(jsonPath("$.[*].seatCapacity").value(hasItem(DEFAULT_SEAT_CAPACITY)))
            .andExpect(jsonPath("$.[*].fareMultiplier").value(hasItem(sameNumber(DEFAULT_FARE_MULTIPLIER))));
    }

    @Test
    @Transactional
    void getCoachType() throws Exception {
        // Initialize the database
        insertedCoachType = coachTypeRepository.saveAndFlush(coachType);

        // Get the coachType
        restCoachTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, coachType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coachType.getId().intValue()))
            .andExpect(jsonPath("$.coachId").value(DEFAULT_COACH_ID))
            .andExpect(jsonPath("$.coachName").value(DEFAULT_COACH_NAME))
            .andExpect(jsonPath("$.seatCapacity").value(DEFAULT_SEAT_CAPACITY))
            .andExpect(jsonPath("$.fareMultiplier").value(sameNumber(DEFAULT_FARE_MULTIPLIER)));
    }

    @Test
    @Transactional
    void getNonExistingCoachType() throws Exception {
        // Get the coachType
        restCoachTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoachType() throws Exception {
        // Initialize the database
        insertedCoachType = coachTypeRepository.saveAndFlush(coachType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coachType
        CoachType updatedCoachType = coachTypeRepository.findById(coachType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoachType are not directly saved in db
        em.detach(updatedCoachType);
        updatedCoachType
            .coachId(UPDATED_COACH_ID)
            .coachName(UPDATED_COACH_NAME)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .fareMultiplier(UPDATED_FARE_MULTIPLIER);
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(updatedCoachType);

        restCoachTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coachTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coachTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoachTypeToMatchAllProperties(updatedCoachType);
    }

    @Test
    @Transactional
    void putNonExistingCoachType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachType.setId(longCount.incrementAndGet());

        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoachTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coachTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coachTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoachType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachType.setId(longCount.incrementAndGet());

        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coachTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoachType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachType.setId(longCount.incrementAndGet());

        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoachTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCoachType = coachTypeRepository.saveAndFlush(coachType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coachType using partial update
        CoachType partialUpdatedCoachType = new CoachType();
        partialUpdatedCoachType.setId(coachType.getId());

        partialUpdatedCoachType.coachId(UPDATED_COACH_ID).fareMultiplier(UPDATED_FARE_MULTIPLIER);

        restCoachTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoachType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoachType))
            )
            .andExpect(status().isOk());

        // Validate the CoachType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoachTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCoachType, coachType),
            getPersistedCoachType(coachType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCoachTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCoachType = coachTypeRepository.saveAndFlush(coachType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coachType using partial update
        CoachType partialUpdatedCoachType = new CoachType();
        partialUpdatedCoachType.setId(coachType.getId());

        partialUpdatedCoachType
            .coachId(UPDATED_COACH_ID)
            .coachName(UPDATED_COACH_NAME)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .fareMultiplier(UPDATED_FARE_MULTIPLIER);

        restCoachTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoachType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoachType))
            )
            .andExpect(status().isOk());

        // Validate the CoachType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoachTypeUpdatableFieldsEquals(partialUpdatedCoachType, getPersistedCoachType(partialUpdatedCoachType));
    }

    @Test
    @Transactional
    void patchNonExistingCoachType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachType.setId(longCount.incrementAndGet());

        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoachTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coachTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coachTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoachType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachType.setId(longCount.incrementAndGet());

        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coachTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoachType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coachType.setId(longCount.incrementAndGet());

        // Create the CoachType
        CoachTypeDTO coachTypeDTO = coachTypeMapper.toDto(coachType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coachTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoachType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoachType() throws Exception {
        // Initialize the database
        insertedCoachType = coachTypeRepository.saveAndFlush(coachType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coachType
        restCoachTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, coachType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coachTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CoachType getPersistedCoachType(CoachType coachType) {
        return coachTypeRepository.findById(coachType.getId()).orElseThrow();
    }

    protected void assertPersistedCoachTypeToMatchAllProperties(CoachType expectedCoachType) {
        assertCoachTypeAllPropertiesEquals(expectedCoachType, getPersistedCoachType(expectedCoachType));
    }

    protected void assertPersistedCoachTypeToMatchUpdatableProperties(CoachType expectedCoachType) {
        assertCoachTypeAllUpdatablePropertiesEquals(expectedCoachType, getPersistedCoachType(expectedCoachType));
    }
}
