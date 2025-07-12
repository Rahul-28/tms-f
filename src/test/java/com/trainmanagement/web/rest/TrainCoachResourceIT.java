package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.TrainCoachAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trainmanagement.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.TrainCoach;
import com.trainmanagement.repository.TrainCoachRepository;
import com.trainmanagement.service.dto.TrainCoachDTO;
import com.trainmanagement.service.mapper.TrainCoachMapper;
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
 * Integration tests for the {@link TrainCoachResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainCoachResourceIT {

    private static final String DEFAULT_TRAIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRAIN_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEAT_CAPACITY = 1;
    private static final Integer UPDATED_SEAT_CAPACITY = 2;

    private static final Integer DEFAULT_AVAILABLE_SEATS = 1;
    private static final Integer UPDATED_AVAILABLE_SEATS = 2;

    private static final BigDecimal DEFAULT_FARE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FARE_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/train-coaches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainCoachRepository trainCoachRepository;

    @Autowired
    private TrainCoachMapper trainCoachMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainCoachMockMvc;

    private TrainCoach trainCoach;

    private TrainCoach insertedTrainCoach;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainCoach createEntity() {
        return new TrainCoach()
            .trainNumber(DEFAULT_TRAIN_NUMBER)
            .seatCapacity(DEFAULT_SEAT_CAPACITY)
            .availableSeats(DEFAULT_AVAILABLE_SEATS)
            .farePrice(DEFAULT_FARE_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainCoach createUpdatedEntity() {
        return new TrainCoach()
            .trainNumber(UPDATED_TRAIN_NUMBER)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .availableSeats(UPDATED_AVAILABLE_SEATS)
            .farePrice(UPDATED_FARE_PRICE);
    }

    @BeforeEach
    void initTest() {
        trainCoach = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrainCoach != null) {
            trainCoachRepository.delete(insertedTrainCoach);
            insertedTrainCoach = null;
        }
    }

    @Test
    @Transactional
    void createTrainCoach() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);
        var returnedTrainCoachDTO = om.readValue(
            restTrainCoachMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrainCoachDTO.class
        );

        // Validate the TrainCoach in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrainCoach = trainCoachMapper.toEntity(returnedTrainCoachDTO);
        assertTrainCoachUpdatableFieldsEquals(returnedTrainCoach, getPersistedTrainCoach(returnedTrainCoach));

        insertedTrainCoach = returnedTrainCoach;
    }

    @Test
    @Transactional
    void createTrainCoachWithExistingId() throws Exception {
        // Create the TrainCoach with an existing ID
        trainCoach.setId(1L);
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrainNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainCoach.setTrainNumber(null);

        // Create the TrainCoach, which fails.
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        restTrainCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeatCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainCoach.setSeatCapacity(null);

        // Create the TrainCoach, which fails.
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        restTrainCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableSeatsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainCoach.setAvailableSeats(null);

        // Create the TrainCoach, which fails.
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        restTrainCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFarePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainCoach.setFarePrice(null);

        // Create the TrainCoach, which fails.
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        restTrainCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainCoaches() throws Exception {
        // Initialize the database
        insertedTrainCoach = trainCoachRepository.saveAndFlush(trainCoach);

        // Get all the trainCoachList
        restTrainCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainCoach.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainNumber").value(hasItem(DEFAULT_TRAIN_NUMBER)))
            .andExpect(jsonPath("$.[*].seatCapacity").value(hasItem(DEFAULT_SEAT_CAPACITY)))
            .andExpect(jsonPath("$.[*].availableSeats").value(hasItem(DEFAULT_AVAILABLE_SEATS)))
            .andExpect(jsonPath("$.[*].farePrice").value(hasItem(sameNumber(DEFAULT_FARE_PRICE))));
    }

    @Test
    @Transactional
    void getTrainCoach() throws Exception {
        // Initialize the database
        insertedTrainCoach = trainCoachRepository.saveAndFlush(trainCoach);

        // Get the trainCoach
        restTrainCoachMockMvc
            .perform(get(ENTITY_API_URL_ID, trainCoach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainCoach.getId().intValue()))
            .andExpect(jsonPath("$.trainNumber").value(DEFAULT_TRAIN_NUMBER))
            .andExpect(jsonPath("$.seatCapacity").value(DEFAULT_SEAT_CAPACITY))
            .andExpect(jsonPath("$.availableSeats").value(DEFAULT_AVAILABLE_SEATS))
            .andExpect(jsonPath("$.farePrice").value(sameNumber(DEFAULT_FARE_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingTrainCoach() throws Exception {
        // Get the trainCoach
        restTrainCoachMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrainCoach() throws Exception {
        // Initialize the database
        insertedTrainCoach = trainCoachRepository.saveAndFlush(trainCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainCoach
        TrainCoach updatedTrainCoach = trainCoachRepository.findById(trainCoach.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrainCoach are not directly saved in db
        em.detach(updatedTrainCoach);
        updatedTrainCoach
            .trainNumber(UPDATED_TRAIN_NUMBER)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .availableSeats(UPDATED_AVAILABLE_SEATS)
            .farePrice(UPDATED_FARE_PRICE);
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(updatedTrainCoach);

        restTrainCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainCoachDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainCoachDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainCoachToMatchAllProperties(updatedTrainCoach);
    }

    @Test
    @Transactional
    void putNonExistingTrainCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainCoach.setId(longCount.incrementAndGet());

        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainCoachDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainCoachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainCoach.setId(longCount.incrementAndGet());

        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainCoachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainCoach.setId(longCount.incrementAndGet());

        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainCoachMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainCoachWithPatch() throws Exception {
        // Initialize the database
        insertedTrainCoach = trainCoachRepository.saveAndFlush(trainCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainCoach using partial update
        TrainCoach partialUpdatedTrainCoach = new TrainCoach();
        partialUpdatedTrainCoach.setId(trainCoach.getId());

        partialUpdatedTrainCoach.availableSeats(UPDATED_AVAILABLE_SEATS);

        restTrainCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainCoach))
            )
            .andExpect(status().isOk());

        // Validate the TrainCoach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainCoachUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrainCoach, trainCoach),
            getPersistedTrainCoach(trainCoach)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrainCoachWithPatch() throws Exception {
        // Initialize the database
        insertedTrainCoach = trainCoachRepository.saveAndFlush(trainCoach);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainCoach using partial update
        TrainCoach partialUpdatedTrainCoach = new TrainCoach();
        partialUpdatedTrainCoach.setId(trainCoach.getId());

        partialUpdatedTrainCoach
            .trainNumber(UPDATED_TRAIN_NUMBER)
            .seatCapacity(UPDATED_SEAT_CAPACITY)
            .availableSeats(UPDATED_AVAILABLE_SEATS)
            .farePrice(UPDATED_FARE_PRICE);

        restTrainCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainCoach))
            )
            .andExpect(status().isOk());

        // Validate the TrainCoach in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainCoachUpdatableFieldsEquals(partialUpdatedTrainCoach, getPersistedTrainCoach(partialUpdatedTrainCoach));
    }

    @Test
    @Transactional
    void patchNonExistingTrainCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainCoach.setId(longCount.incrementAndGet());

        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainCoachDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainCoachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainCoach.setId(longCount.incrementAndGet());

        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainCoachDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainCoach() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainCoach.setId(longCount.incrementAndGet());

        // Create the TrainCoach
        TrainCoachDTO trainCoachDTO = trainCoachMapper.toDto(trainCoach);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainCoachMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trainCoachDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainCoach in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainCoach() throws Exception {
        // Initialize the database
        insertedTrainCoach = trainCoachRepository.saveAndFlush(trainCoach);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trainCoach
        restTrainCoachMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainCoach.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainCoachRepository.count();
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

    protected TrainCoach getPersistedTrainCoach(TrainCoach trainCoach) {
        return trainCoachRepository.findById(trainCoach.getId()).orElseThrow();
    }

    protected void assertPersistedTrainCoachToMatchAllProperties(TrainCoach expectedTrainCoach) {
        assertTrainCoachAllPropertiesEquals(expectedTrainCoach, getPersistedTrainCoach(expectedTrainCoach));
    }

    protected void assertPersistedTrainCoachToMatchUpdatableProperties(TrainCoach expectedTrainCoach) {
        assertTrainCoachAllUpdatablePropertiesEquals(expectedTrainCoach, getPersistedTrainCoach(expectedTrainCoach));
    }
}
