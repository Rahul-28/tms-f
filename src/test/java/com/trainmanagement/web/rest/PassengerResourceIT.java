package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.PassengerAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.Passenger;
import com.trainmanagement.repository.PassengerRepository;
import com.trainmanagement.service.dto.PassengerDTO;
import com.trainmanagement.service.mapper.PassengerMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PassengerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PassengerResourceIT {

    private static final String DEFAULT_PASSENGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PASSENGER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_COACH_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COACH_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SEAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SEAT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/passengers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    private Passenger insertedPassenger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity() {
        return new Passenger()
            .passengerName(DEFAULT_PASSENGER_NAME)
            .age(DEFAULT_AGE)
            .coachNumber(DEFAULT_COACH_NUMBER)
            .seatNumber(DEFAULT_SEAT_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createUpdatedEntity() {
        return new Passenger()
            .passengerName(UPDATED_PASSENGER_NAME)
            .age(UPDATED_AGE)
            .coachNumber(UPDATED_COACH_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);
    }

    @BeforeEach
    void initTest() {
        passenger = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPassenger != null) {
            passengerRepository.delete(insertedPassenger);
            insertedPassenger = null;
        }
    }

    @Test
    @Transactional
    void createPassenger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);
        var returnedPassengerDTO = om.readValue(
            restPassengerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PassengerDTO.class
        );

        // Validate the Passenger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPassenger = passengerMapper.toEntity(returnedPassengerDTO);
        assertPassengerUpdatableFieldsEquals(returnedPassenger, getPersistedPassenger(returnedPassenger));

        insertedPassenger = returnedPassenger;
    }

    @Test
    @Transactional
    void createPassengerWithExistingId() throws Exception {
        // Create the Passenger with an existing ID
        passenger.setId(1L);
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPassengerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passenger.setPassengerName(null);

        // Create the Passenger, which fails.
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passenger.setAge(null);

        // Create the Passenger, which fails.
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoachNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passenger.setCoachNumber(null);

        // Create the Passenger, which fails.
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeatNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passenger.setSeatNumber(null);

        // Create the Passenger, which fails.
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPassengers() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].passengerName").value(hasItem(DEFAULT_PASSENGER_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].coachNumber").value(hasItem(DEFAULT_COACH_NUMBER)))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)));
    }

    @Test
    @Transactional
    void getPassenger() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc
            .perform(get(ENTITY_API_URL_ID, passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.passengerName").value(DEFAULT_PASSENGER_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.coachNumber").value(DEFAULT_COACH_NUMBER))
            .andExpect(jsonPath("$.seatNumber").value(DEFAULT_SEAT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPassenger() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .passengerName(UPDATED_PASSENGER_NAME)
            .age(UPDATED_AGE)
            .coachNumber(UPDATED_COACH_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);
        PassengerDTO passengerDTO = passengerMapper.toDto(updatedPassenger);

        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passengerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPassengerToMatchAllProperties(updatedPassenger);
    }

    @Test
    @Transactional
    void putNonExistingPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passengerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassengerWithPatch() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passenger using partial update
        Passenger partialUpdatedPassenger = new Passenger();
        partialUpdatedPassenger.setId(passenger.getId());

        partialUpdatedPassenger.age(UPDATED_AGE).coachNumber(UPDATED_COACH_NUMBER).seatNumber(UPDATED_SEAT_NUMBER);

        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassengerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPassenger, passenger),
            getPersistedPassenger(passenger)
        );
    }

    @Test
    @Transactional
    void fullUpdatePassengerWithPatch() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passenger using partial update
        Passenger partialUpdatedPassenger = new Passenger();
        partialUpdatedPassenger.setId(passenger.getId());

        partialUpdatedPassenger
            .passengerName(UPDATED_PASSENGER_NAME)
            .age(UPDATED_AGE)
            .coachNumber(UPDATED_COACH_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);

        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassengerUpdatableFieldsEquals(partialUpdatedPassenger, getPersistedPassenger(partialUpdatedPassenger));
    }

    @Test
    @Transactional
    void patchNonExistingPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passengerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassenger() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the passenger
        restPassengerMockMvc
            .perform(delete(ENTITY_API_URL_ID, passenger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return passengerRepository.count();
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

    protected Passenger getPersistedPassenger(Passenger passenger) {
        return passengerRepository.findById(passenger.getId()).orElseThrow();
    }

    protected void assertPersistedPassengerToMatchAllProperties(Passenger expectedPassenger) {
        assertPassengerAllPropertiesEquals(expectedPassenger, getPersistedPassenger(expectedPassenger));
    }

    protected void assertPersistedPassengerToMatchUpdatableProperties(Passenger expectedPassenger) {
        assertPassengerAllUpdatablePropertiesEquals(expectedPassenger, getPersistedPassenger(expectedPassenger));
    }
}
