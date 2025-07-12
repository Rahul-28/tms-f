package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.TrainAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trainmanagement.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.Train;
import com.trainmanagement.domain.enumeration.ServiceType;
import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.service.dto.TrainDTO;
import com.trainmanagement.service.mapper.TrainMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TrainResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainResourceIT {

    private static final String DEFAULT_TRAIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRAIN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TRAIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRAIN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_INTERMEDIATE_STOP = "AAAAAAAAAA";
    private static final String UPDATED_INTERMEDIATE_STOP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SERVICE_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SERVICE_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SERVICE_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_SERVICE_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SERVICE_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SERVICE_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final ServiceType DEFAULT_SERVICE_TYPE = ServiceType.DAILY;
    private static final ServiceType UPDATED_SERVICE_TYPE = ServiceType.WEEKLY;

    private static final Instant DEFAULT_DEPARTURE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEPARTURE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARRIVAL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARRIVAL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_BASIC_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASIC_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BASIC_PRICE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/trains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TrainMapper trainMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainMockMvc;

    private Train train;

    private Train insertedTrain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Train createEntity() {
        return new Train()
            .trainNumber(DEFAULT_TRAIN_NUMBER)
            .trainName(DEFAULT_TRAIN_NAME)
            .origin(DEFAULT_ORIGIN)
            .destination(DEFAULT_DESTINATION)
            .intermediateStop(DEFAULT_INTERMEDIATE_STOP)
            .serviceStartDate(DEFAULT_SERVICE_START_DATE)
            .serviceEndDate(DEFAULT_SERVICE_END_DATE)
            .serviceType(DEFAULT_SERVICE_TYPE)
            .departureTime(DEFAULT_DEPARTURE_TIME)
            .arrivalTime(DEFAULT_ARRIVAL_TIME)
            .basicPrice(DEFAULT_BASIC_PRICE)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Train createUpdatedEntity() {
        return new Train()
            .trainNumber(UPDATED_TRAIN_NUMBER)
            .trainName(UPDATED_TRAIN_NAME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .intermediateStop(UPDATED_INTERMEDIATE_STOP)
            .serviceStartDate(UPDATED_SERVICE_START_DATE)
            .serviceEndDate(UPDATED_SERVICE_END_DATE)
            .serviceType(UPDATED_SERVICE_TYPE)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .basicPrice(UPDATED_BASIC_PRICE)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        train = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrain != null) {
            trainRepository.delete(insertedTrain);
            insertedTrain = null;
        }
    }

    @Test
    @Transactional
    void createTrain() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);
        var returnedTrainDTO = om.readValue(
            restTrainMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrainDTO.class
        );

        // Validate the Train in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrain = trainMapper.toEntity(returnedTrainDTO);
        assertTrainUpdatableFieldsEquals(returnedTrain, getPersistedTrain(returnedTrain));

        insertedTrain = returnedTrain;
    }

    @Test
    @Transactional
    void createTrainWithExistingId() throws Exception {
        // Create the Train with an existing ID
        train.setId(1L);
        TrainDTO trainDTO = trainMapper.toDto(train);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrainNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setTrainNumber(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrainNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setTrainName(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOriginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setOrigin(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setDestination(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntermediateStopIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setIntermediateStop(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setServiceStartDate(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setServiceEndDate(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setServiceType(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartureTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setDepartureTime(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArrivalTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setArrivalTime(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBasicPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setBasicPrice(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        train.setIsActive(null);

        // Create the Train, which fails.
        TrainDTO trainDTO = trainMapper.toDto(train);

        restTrainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrains() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList
        restTrainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(train.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainNumber").value(hasItem(DEFAULT_TRAIN_NUMBER)))
            .andExpect(jsonPath("$.[*].trainName").value(hasItem(DEFAULT_TRAIN_NAME)))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].intermediateStop").value(hasItem(DEFAULT_INTERMEDIATE_STOP)))
            .andExpect(jsonPath("$.[*].serviceStartDate").value(hasItem(DEFAULT_SERVICE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].serviceEndDate").value(hasItem(DEFAULT_SERVICE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(DEFAULT_DEPARTURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(DEFAULT_ARRIVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].basicPrice").value(hasItem(sameNumber(DEFAULT_BASIC_PRICE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getTrain() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get the train
        restTrainMockMvc
            .perform(get(ENTITY_API_URL_ID, train.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(train.getId().intValue()))
            .andExpect(jsonPath("$.trainNumber").value(DEFAULT_TRAIN_NUMBER))
            .andExpect(jsonPath("$.trainName").value(DEFAULT_TRAIN_NAME))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.intermediateStop").value(DEFAULT_INTERMEDIATE_STOP))
            .andExpect(jsonPath("$.serviceStartDate").value(DEFAULT_SERVICE_START_DATE.toString()))
            .andExpect(jsonPath("$.serviceEndDate").value(DEFAULT_SERVICE_END_DATE.toString()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.departureTime").value(DEFAULT_DEPARTURE_TIME.toString()))
            .andExpect(jsonPath("$.arrivalTime").value(DEFAULT_ARRIVAL_TIME.toString()))
            .andExpect(jsonPath("$.basicPrice").value(sameNumber(DEFAULT_BASIC_PRICE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getTrainsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        Long id = train.getId();

        defaultTrainFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrainFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrainFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainNumber equals to
        defaultTrainFiltering("trainNumber.equals=" + DEFAULT_TRAIN_NUMBER, "trainNumber.equals=" + UPDATED_TRAIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainNumber in
        defaultTrainFiltering(
            "trainNumber.in=" + DEFAULT_TRAIN_NUMBER + "," + UPDATED_TRAIN_NUMBER,
            "trainNumber.in=" + UPDATED_TRAIN_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainNumber is not null
        defaultTrainFiltering("trainNumber.specified=true", "trainNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainNumber contains
        defaultTrainFiltering("trainNumber.contains=" + DEFAULT_TRAIN_NUMBER, "trainNumber.contains=" + UPDATED_TRAIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainNumber does not contain
        defaultTrainFiltering("trainNumber.doesNotContain=" + UPDATED_TRAIN_NUMBER, "trainNumber.doesNotContain=" + DEFAULT_TRAIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainName equals to
        defaultTrainFiltering("trainName.equals=" + DEFAULT_TRAIN_NAME, "trainName.equals=" + UPDATED_TRAIN_NAME);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainName in
        defaultTrainFiltering("trainName.in=" + DEFAULT_TRAIN_NAME + "," + UPDATED_TRAIN_NAME, "trainName.in=" + UPDATED_TRAIN_NAME);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainName is not null
        defaultTrainFiltering("trainName.specified=true", "trainName.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainName contains
        defaultTrainFiltering("trainName.contains=" + DEFAULT_TRAIN_NAME, "trainName.contains=" + UPDATED_TRAIN_NAME);
    }

    @Test
    @Transactional
    void getAllTrainsByTrainNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where trainName does not contain
        defaultTrainFiltering("trainName.doesNotContain=" + UPDATED_TRAIN_NAME, "trainName.doesNotContain=" + DEFAULT_TRAIN_NAME);
    }

    @Test
    @Transactional
    void getAllTrainsByOriginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where origin equals to
        defaultTrainFiltering("origin.equals=" + DEFAULT_ORIGIN, "origin.equals=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllTrainsByOriginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where origin in
        defaultTrainFiltering("origin.in=" + DEFAULT_ORIGIN + "," + UPDATED_ORIGIN, "origin.in=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllTrainsByOriginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where origin is not null
        defaultTrainFiltering("origin.specified=true", "origin.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByOriginContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where origin contains
        defaultTrainFiltering("origin.contains=" + DEFAULT_ORIGIN, "origin.contains=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllTrainsByOriginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where origin does not contain
        defaultTrainFiltering("origin.doesNotContain=" + UPDATED_ORIGIN, "origin.doesNotContain=" + DEFAULT_ORIGIN);
    }

    @Test
    @Transactional
    void getAllTrainsByDestinationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where destination equals to
        defaultTrainFiltering("destination.equals=" + DEFAULT_DESTINATION, "destination.equals=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTrainsByDestinationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where destination in
        defaultTrainFiltering("destination.in=" + DEFAULT_DESTINATION + "," + UPDATED_DESTINATION, "destination.in=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTrainsByDestinationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where destination is not null
        defaultTrainFiltering("destination.specified=true", "destination.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByDestinationContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where destination contains
        defaultTrainFiltering("destination.contains=" + DEFAULT_DESTINATION, "destination.contains=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTrainsByDestinationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where destination does not contain
        defaultTrainFiltering("destination.doesNotContain=" + UPDATED_DESTINATION, "destination.doesNotContain=" + DEFAULT_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTrainsByIntermediateStopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where intermediateStop equals to
        defaultTrainFiltering(
            "intermediateStop.equals=" + DEFAULT_INTERMEDIATE_STOP,
            "intermediateStop.equals=" + UPDATED_INTERMEDIATE_STOP
        );
    }

    @Test
    @Transactional
    void getAllTrainsByIntermediateStopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where intermediateStop in
        defaultTrainFiltering(
            "intermediateStop.in=" + DEFAULT_INTERMEDIATE_STOP + "," + UPDATED_INTERMEDIATE_STOP,
            "intermediateStop.in=" + UPDATED_INTERMEDIATE_STOP
        );
    }

    @Test
    @Transactional
    void getAllTrainsByIntermediateStopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where intermediateStop is not null
        defaultTrainFiltering("intermediateStop.specified=true", "intermediateStop.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByIntermediateStopContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where intermediateStop contains
        defaultTrainFiltering(
            "intermediateStop.contains=" + DEFAULT_INTERMEDIATE_STOP,
            "intermediateStop.contains=" + UPDATED_INTERMEDIATE_STOP
        );
    }

    @Test
    @Transactional
    void getAllTrainsByIntermediateStopNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where intermediateStop does not contain
        defaultTrainFiltering(
            "intermediateStop.doesNotContain=" + UPDATED_INTERMEDIATE_STOP,
            "intermediateStop.doesNotContain=" + DEFAULT_INTERMEDIATE_STOP
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate equals to
        defaultTrainFiltering(
            "serviceStartDate.equals=" + DEFAULT_SERVICE_START_DATE,
            "serviceStartDate.equals=" + UPDATED_SERVICE_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate in
        defaultTrainFiltering(
            "serviceStartDate.in=" + DEFAULT_SERVICE_START_DATE + "," + UPDATED_SERVICE_START_DATE,
            "serviceStartDate.in=" + UPDATED_SERVICE_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate is not null
        defaultTrainFiltering("serviceStartDate.specified=true", "serviceStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate is greater than or equal to
        defaultTrainFiltering(
            "serviceStartDate.greaterThanOrEqual=" + DEFAULT_SERVICE_START_DATE,
            "serviceStartDate.greaterThanOrEqual=" + UPDATED_SERVICE_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate is less than or equal to
        defaultTrainFiltering(
            "serviceStartDate.lessThanOrEqual=" + DEFAULT_SERVICE_START_DATE,
            "serviceStartDate.lessThanOrEqual=" + SMALLER_SERVICE_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate is less than
        defaultTrainFiltering(
            "serviceStartDate.lessThan=" + UPDATED_SERVICE_START_DATE,
            "serviceStartDate.lessThan=" + DEFAULT_SERVICE_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceStartDate is greater than
        defaultTrainFiltering(
            "serviceStartDate.greaterThan=" + SMALLER_SERVICE_START_DATE,
            "serviceStartDate.greaterThan=" + DEFAULT_SERVICE_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate equals to
        defaultTrainFiltering("serviceEndDate.equals=" + DEFAULT_SERVICE_END_DATE, "serviceEndDate.equals=" + UPDATED_SERVICE_END_DATE);
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate in
        defaultTrainFiltering(
            "serviceEndDate.in=" + DEFAULT_SERVICE_END_DATE + "," + UPDATED_SERVICE_END_DATE,
            "serviceEndDate.in=" + UPDATED_SERVICE_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate is not null
        defaultTrainFiltering("serviceEndDate.specified=true", "serviceEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate is greater than or equal to
        defaultTrainFiltering(
            "serviceEndDate.greaterThanOrEqual=" + DEFAULT_SERVICE_END_DATE,
            "serviceEndDate.greaterThanOrEqual=" + UPDATED_SERVICE_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate is less than or equal to
        defaultTrainFiltering(
            "serviceEndDate.lessThanOrEqual=" + DEFAULT_SERVICE_END_DATE,
            "serviceEndDate.lessThanOrEqual=" + SMALLER_SERVICE_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate is less than
        defaultTrainFiltering("serviceEndDate.lessThan=" + UPDATED_SERVICE_END_DATE, "serviceEndDate.lessThan=" + DEFAULT_SERVICE_END_DATE);
    }

    @Test
    @Transactional
    void getAllTrainsByServiceEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceEndDate is greater than
        defaultTrainFiltering(
            "serviceEndDate.greaterThan=" + SMALLER_SERVICE_END_DATE,
            "serviceEndDate.greaterThan=" + DEFAULT_SERVICE_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceType equals to
        defaultTrainFiltering("serviceType.equals=" + DEFAULT_SERVICE_TYPE, "serviceType.equals=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTrainsByServiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceType in
        defaultTrainFiltering(
            "serviceType.in=" + DEFAULT_SERVICE_TYPE + "," + UPDATED_SERVICE_TYPE,
            "serviceType.in=" + UPDATED_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByServiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where serviceType is not null
        defaultTrainFiltering("serviceType.specified=true", "serviceType.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByDepartureTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where departureTime equals to
        defaultTrainFiltering("departureTime.equals=" + DEFAULT_DEPARTURE_TIME, "departureTime.equals=" + UPDATED_DEPARTURE_TIME);
    }

    @Test
    @Transactional
    void getAllTrainsByDepartureTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where departureTime in
        defaultTrainFiltering(
            "departureTime.in=" + DEFAULT_DEPARTURE_TIME + "," + UPDATED_DEPARTURE_TIME,
            "departureTime.in=" + UPDATED_DEPARTURE_TIME
        );
    }

    @Test
    @Transactional
    void getAllTrainsByDepartureTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where departureTime is not null
        defaultTrainFiltering("departureTime.specified=true", "departureTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByArrivalTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where arrivalTime equals to
        defaultTrainFiltering("arrivalTime.equals=" + DEFAULT_ARRIVAL_TIME, "arrivalTime.equals=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    void getAllTrainsByArrivalTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where arrivalTime in
        defaultTrainFiltering(
            "arrivalTime.in=" + DEFAULT_ARRIVAL_TIME + "," + UPDATED_ARRIVAL_TIME,
            "arrivalTime.in=" + UPDATED_ARRIVAL_TIME
        );
    }

    @Test
    @Transactional
    void getAllTrainsByArrivalTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where arrivalTime is not null
        defaultTrainFiltering("arrivalTime.specified=true", "arrivalTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice equals to
        defaultTrainFiltering("basicPrice.equals=" + DEFAULT_BASIC_PRICE, "basicPrice.equals=" + UPDATED_BASIC_PRICE);
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice in
        defaultTrainFiltering("basicPrice.in=" + DEFAULT_BASIC_PRICE + "," + UPDATED_BASIC_PRICE, "basicPrice.in=" + UPDATED_BASIC_PRICE);
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice is not null
        defaultTrainFiltering("basicPrice.specified=true", "basicPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice is greater than or equal to
        defaultTrainFiltering(
            "basicPrice.greaterThanOrEqual=" + DEFAULT_BASIC_PRICE,
            "basicPrice.greaterThanOrEqual=" + UPDATED_BASIC_PRICE
        );
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice is less than or equal to
        defaultTrainFiltering("basicPrice.lessThanOrEqual=" + DEFAULT_BASIC_PRICE, "basicPrice.lessThanOrEqual=" + SMALLER_BASIC_PRICE);
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice is less than
        defaultTrainFiltering("basicPrice.lessThan=" + UPDATED_BASIC_PRICE, "basicPrice.lessThan=" + DEFAULT_BASIC_PRICE);
    }

    @Test
    @Transactional
    void getAllTrainsByBasicPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where basicPrice is greater than
        defaultTrainFiltering("basicPrice.greaterThan=" + SMALLER_BASIC_PRICE, "basicPrice.greaterThan=" + DEFAULT_BASIC_PRICE);
    }

    @Test
    @Transactional
    void getAllTrainsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where isActive equals to
        defaultTrainFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTrainsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where isActive in
        defaultTrainFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTrainsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        // Get all the trainList where isActive is not null
        defaultTrainFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultTrainFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrainShouldBeFound(shouldBeFound);
        defaultTrainShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrainShouldBeFound(String filter) throws Exception {
        restTrainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(train.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainNumber").value(hasItem(DEFAULT_TRAIN_NUMBER)))
            .andExpect(jsonPath("$.[*].trainName").value(hasItem(DEFAULT_TRAIN_NAME)))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].intermediateStop").value(hasItem(DEFAULT_INTERMEDIATE_STOP)))
            .andExpect(jsonPath("$.[*].serviceStartDate").value(hasItem(DEFAULT_SERVICE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].serviceEndDate").value(hasItem(DEFAULT_SERVICE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(DEFAULT_DEPARTURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(DEFAULT_ARRIVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].basicPrice").value(hasItem(sameNumber(DEFAULT_BASIC_PRICE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restTrainMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrainShouldNotBeFound(String filter) throws Exception {
        restTrainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrainMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrain() throws Exception {
        // Get the train
        restTrainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrain() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the train
        Train updatedTrain = trainRepository.findById(train.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrain are not directly saved in db
        em.detach(updatedTrain);
        updatedTrain
            .trainNumber(UPDATED_TRAIN_NUMBER)
            .trainName(UPDATED_TRAIN_NAME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .intermediateStop(UPDATED_INTERMEDIATE_STOP)
            .serviceStartDate(UPDATED_SERVICE_START_DATE)
            .serviceEndDate(UPDATED_SERVICE_END_DATE)
            .serviceType(UPDATED_SERVICE_TYPE)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .basicPrice(UPDATED_BASIC_PRICE)
            .isActive(UPDATED_IS_ACTIVE);
        TrainDTO trainDTO = trainMapper.toDto(updatedTrain);

        restTrainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO))
            )
            .andExpect(status().isOk());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainToMatchAllProperties(updatedTrain);
    }

    @Test
    @Transactional
    void putNonExistingTrain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        train.setId(longCount.incrementAndGet());

        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        train.setId(longCount.incrementAndGet());

        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        train.setId(longCount.incrementAndGet());

        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainWithPatch() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the train using partial update
        Train partialUpdatedTrain = new Train();
        partialUpdatedTrain.setId(train.getId());

        partialUpdatedTrain
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .intermediateStop(UPDATED_INTERMEDIATE_STOP)
            .serviceStartDate(UPDATED_SERVICE_START_DATE)
            .serviceType(UPDATED_SERVICE_TYPE)
            .arrivalTime(UPDATED_ARRIVAL_TIME);

        restTrainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrain.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrain))
            )
            .andExpect(status().isOk());

        // Validate the Train in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrain, train), getPersistedTrain(train));
    }

    @Test
    @Transactional
    void fullUpdateTrainWithPatch() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the train using partial update
        Train partialUpdatedTrain = new Train();
        partialUpdatedTrain.setId(train.getId());

        partialUpdatedTrain
            .trainNumber(UPDATED_TRAIN_NUMBER)
            .trainName(UPDATED_TRAIN_NAME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .intermediateStop(UPDATED_INTERMEDIATE_STOP)
            .serviceStartDate(UPDATED_SERVICE_START_DATE)
            .serviceEndDate(UPDATED_SERVICE_END_DATE)
            .serviceType(UPDATED_SERVICE_TYPE)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .basicPrice(UPDATED_BASIC_PRICE)
            .isActive(UPDATED_IS_ACTIVE);

        restTrainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrain.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrain))
            )
            .andExpect(status().isOk());

        // Validate the Train in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainUpdatableFieldsEquals(partialUpdatedTrain, getPersistedTrain(partialUpdatedTrain));
    }

    @Test
    @Transactional
    void patchNonExistingTrain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        train.setId(longCount.incrementAndGet());

        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        train.setId(longCount.incrementAndGet());

        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrain() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        train.setId(longCount.incrementAndGet());

        // Create the Train
        TrainDTO trainDTO = trainMapper.toDto(train);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trainDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Train in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrain() throws Exception {
        // Initialize the database
        insertedTrain = trainRepository.saveAndFlush(train);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the train
        restTrainMockMvc
            .perform(delete(ENTITY_API_URL_ID, train.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainRepository.count();
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

    protected Train getPersistedTrain(Train train) {
        return trainRepository.findById(train.getId()).orElseThrow();
    }

    protected void assertPersistedTrainToMatchAllProperties(Train expectedTrain) {
        assertTrainAllPropertiesEquals(expectedTrain, getPersistedTrain(expectedTrain));
    }

    protected void assertPersistedTrainToMatchUpdatableProperties(Train expectedTrain) {
        assertTrainAllUpdatablePropertiesEquals(expectedTrain, getPersistedTrain(expectedTrain));
    }
}
