package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.BookingAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trainmanagement.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.Booking;
import com.trainmanagement.domain.Customer;
import com.trainmanagement.domain.Train;
import com.trainmanagement.domain.enumeration.BookingStatus;
import com.trainmanagement.domain.enumeration.PaymentMode;
import com.trainmanagement.repository.BookingRepository;
import com.trainmanagement.service.dto.BookingDTO;
import com.trainmanagement.service.mapper.BookingMapper;
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
 * Integration tests for the {@link BookingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookingResourceIT {

    private static final String DEFAULT_PNR_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PNR_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_BOOKING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BOOKING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_TRAVELLING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRAVELLING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRAVELLING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BOARDING_STATION = "AAAAAAAAAA";
    private static final String UPDATED_BOARDING_STATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_STATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_STATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_BOARDING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BOARDING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARRIVAL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARRIVAL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_FARE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_FARE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_FARE = new BigDecimal(1 - 1);

    private static final BookingStatus DEFAULT_BOOKING_STATUS = BookingStatus.CONFIRMED;
    private static final BookingStatus UPDATED_BOOKING_STATUS = BookingStatus.CANCELLED;

    private static final PaymentMode DEFAULT_MODE_OF_PAYMENT = PaymentMode.CREDIT_CARD;
    private static final PaymentMode UPDATED_MODE_OF_PAYMENT = PaymentMode.DEBIT_CARD;

    private static final Boolean DEFAULT_ADDITIONAL_SERVICES = false;
    private static final Boolean UPDATED_ADDITIONAL_SERVICES = true;

    private static final String DEFAULT_COACH_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COACH_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SEAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SEAT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    private Booking insertedBooking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity() {
        return new Booking()
            .pnrNumber(DEFAULT_PNR_NUMBER)
            .bookingDate(DEFAULT_BOOKING_DATE)
            .travellingDate(DEFAULT_TRAVELLING_DATE)
            .boardingStation(DEFAULT_BOARDING_STATION)
            .destinationStation(DEFAULT_DESTINATION_STATION)
            .boardingTime(DEFAULT_BOARDING_TIME)
            .arrivalTime(DEFAULT_ARRIVAL_TIME)
            .totalFare(DEFAULT_TOTAL_FARE)
            .bookingStatus(DEFAULT_BOOKING_STATUS)
            .modeOfPayment(DEFAULT_MODE_OF_PAYMENT)
            .additionalServices(DEFAULT_ADDITIONAL_SERVICES)
            .coachNumber(DEFAULT_COACH_NUMBER)
            .seatNumber(DEFAULT_SEAT_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity() {
        return new Booking()
            .pnrNumber(UPDATED_PNR_NUMBER)
            .bookingDate(UPDATED_BOOKING_DATE)
            .travellingDate(UPDATED_TRAVELLING_DATE)
            .boardingStation(UPDATED_BOARDING_STATION)
            .destinationStation(UPDATED_DESTINATION_STATION)
            .boardingTime(UPDATED_BOARDING_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .totalFare(UPDATED_TOTAL_FARE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .additionalServices(UPDATED_ADDITIONAL_SERVICES)
            .coachNumber(UPDATED_COACH_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);
    }

    @BeforeEach
    void initTest() {
        booking = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBooking != null) {
            bookingRepository.delete(insertedBooking);
            insertedBooking = null;
        }
    }

    @Test
    @Transactional
    void createBooking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        var returnedBookingDTO = om.readValue(
            restBookingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingDTO.class
        );

        // Validate the Booking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBooking = bookingMapper.toEntity(returnedBookingDTO);
        assertBookingUpdatableFieldsEquals(returnedBooking, getPersistedBooking(returnedBooking));

        insertedBooking = returnedBooking;
    }

    @Test
    @Transactional
    void createBookingWithExistingId() throws Exception {
        // Create the Booking with an existing ID
        booking.setId(1L);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPnrNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setPnrNumber(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBookingDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setBookingDate(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTravellingDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setTravellingDate(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBoardingStationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setBoardingStation(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationStationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setDestinationStation(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBoardingTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setBoardingTime(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArrivalTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setArrivalTime(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalFareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setTotalFare(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBookingStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setBookingStatus(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModeOfPaymentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setModeOfPayment(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdditionalServicesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setAdditionalServices(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoachNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setCoachNumber(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeatNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setSeatNumber(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookings() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].pnrNumber").value(hasItem(DEFAULT_PNR_NUMBER)))
            .andExpect(jsonPath("$.[*].bookingDate").value(hasItem(DEFAULT_BOOKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].travellingDate").value(hasItem(DEFAULT_TRAVELLING_DATE.toString())))
            .andExpect(jsonPath("$.[*].boardingStation").value(hasItem(DEFAULT_BOARDING_STATION)))
            .andExpect(jsonPath("$.[*].destinationStation").value(hasItem(DEFAULT_DESTINATION_STATION)))
            .andExpect(jsonPath("$.[*].boardingTime").value(hasItem(DEFAULT_BOARDING_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(DEFAULT_ARRIVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalFare").value(hasItem(sameNumber(DEFAULT_TOTAL_FARE))))
            .andExpect(jsonPath("$.[*].bookingStatus").value(hasItem(DEFAULT_BOOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modeOfPayment").value(hasItem(DEFAULT_MODE_OF_PAYMENT.toString())))
            .andExpect(jsonPath("$.[*].additionalServices").value(hasItem(DEFAULT_ADDITIONAL_SERVICES)))
            .andExpect(jsonPath("$.[*].coachNumber").value(hasItem(DEFAULT_COACH_NUMBER)))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)));
    }

    @Test
    @Transactional
    void getBooking() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc
            .perform(get(ENTITY_API_URL_ID, booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.pnrNumber").value(DEFAULT_PNR_NUMBER))
            .andExpect(jsonPath("$.bookingDate").value(DEFAULT_BOOKING_DATE.toString()))
            .andExpect(jsonPath("$.travellingDate").value(DEFAULT_TRAVELLING_DATE.toString()))
            .andExpect(jsonPath("$.boardingStation").value(DEFAULT_BOARDING_STATION))
            .andExpect(jsonPath("$.destinationStation").value(DEFAULT_DESTINATION_STATION))
            .andExpect(jsonPath("$.boardingTime").value(DEFAULT_BOARDING_TIME.toString()))
            .andExpect(jsonPath("$.arrivalTime").value(DEFAULT_ARRIVAL_TIME.toString()))
            .andExpect(jsonPath("$.totalFare").value(sameNumber(DEFAULT_TOTAL_FARE)))
            .andExpect(jsonPath("$.bookingStatus").value(DEFAULT_BOOKING_STATUS.toString()))
            .andExpect(jsonPath("$.modeOfPayment").value(DEFAULT_MODE_OF_PAYMENT.toString()))
            .andExpect(jsonPath("$.additionalServices").value(DEFAULT_ADDITIONAL_SERVICES))
            .andExpect(jsonPath("$.coachNumber").value(DEFAULT_COACH_NUMBER))
            .andExpect(jsonPath("$.seatNumber").value(DEFAULT_SEAT_NUMBER));
    }

    @Test
    @Transactional
    void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        Long id = booking.getId();

        defaultBookingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBookingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBookingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookingsByPnrNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where pnrNumber equals to
        defaultBookingFiltering("pnrNumber.equals=" + DEFAULT_PNR_NUMBER, "pnrNumber.equals=" + UPDATED_PNR_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByPnrNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where pnrNumber in
        defaultBookingFiltering("pnrNumber.in=" + DEFAULT_PNR_NUMBER + "," + UPDATED_PNR_NUMBER, "pnrNumber.in=" + UPDATED_PNR_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByPnrNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where pnrNumber is not null
        defaultBookingFiltering("pnrNumber.specified=true", "pnrNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByPnrNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where pnrNumber contains
        defaultBookingFiltering("pnrNumber.contains=" + DEFAULT_PNR_NUMBER, "pnrNumber.contains=" + UPDATED_PNR_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByPnrNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where pnrNumber does not contain
        defaultBookingFiltering("pnrNumber.doesNotContain=" + UPDATED_PNR_NUMBER, "pnrNumber.doesNotContain=" + DEFAULT_PNR_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingDate equals to
        defaultBookingFiltering("bookingDate.equals=" + DEFAULT_BOOKING_DATE, "bookingDate.equals=" + UPDATED_BOOKING_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingDate in
        defaultBookingFiltering(
            "bookingDate.in=" + DEFAULT_BOOKING_DATE + "," + UPDATED_BOOKING_DATE,
            "bookingDate.in=" + UPDATED_BOOKING_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBookingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingDate is not null
        defaultBookingFiltering("bookingDate.specified=true", "bookingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate equals to
        defaultBookingFiltering("travellingDate.equals=" + DEFAULT_TRAVELLING_DATE, "travellingDate.equals=" + UPDATED_TRAVELLING_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate in
        defaultBookingFiltering(
            "travellingDate.in=" + DEFAULT_TRAVELLING_DATE + "," + UPDATED_TRAVELLING_DATE,
            "travellingDate.in=" + UPDATED_TRAVELLING_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate is not null
        defaultBookingFiltering("travellingDate.specified=true", "travellingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate is greater than or equal to
        defaultBookingFiltering(
            "travellingDate.greaterThanOrEqual=" + DEFAULT_TRAVELLING_DATE,
            "travellingDate.greaterThanOrEqual=" + UPDATED_TRAVELLING_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate is less than or equal to
        defaultBookingFiltering(
            "travellingDate.lessThanOrEqual=" + DEFAULT_TRAVELLING_DATE,
            "travellingDate.lessThanOrEqual=" + SMALLER_TRAVELLING_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate is less than
        defaultBookingFiltering("travellingDate.lessThan=" + UPDATED_TRAVELLING_DATE, "travellingDate.lessThan=" + DEFAULT_TRAVELLING_DATE);
    }

    @Test
    @Transactional
    void getAllBookingsByTravellingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where travellingDate is greater than
        defaultBookingFiltering(
            "travellingDate.greaterThan=" + SMALLER_TRAVELLING_DATE,
            "travellingDate.greaterThan=" + DEFAULT_TRAVELLING_DATE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingStationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingStation equals to
        defaultBookingFiltering("boardingStation.equals=" + DEFAULT_BOARDING_STATION, "boardingStation.equals=" + UPDATED_BOARDING_STATION);
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingStationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingStation in
        defaultBookingFiltering(
            "boardingStation.in=" + DEFAULT_BOARDING_STATION + "," + UPDATED_BOARDING_STATION,
            "boardingStation.in=" + UPDATED_BOARDING_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingStationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingStation is not null
        defaultBookingFiltering("boardingStation.specified=true", "boardingStation.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingStationContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingStation contains
        defaultBookingFiltering(
            "boardingStation.contains=" + DEFAULT_BOARDING_STATION,
            "boardingStation.contains=" + UPDATED_BOARDING_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingStationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingStation does not contain
        defaultBookingFiltering(
            "boardingStation.doesNotContain=" + UPDATED_BOARDING_STATION,
            "boardingStation.doesNotContain=" + DEFAULT_BOARDING_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDestinationStationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where destinationStation equals to
        defaultBookingFiltering(
            "destinationStation.equals=" + DEFAULT_DESTINATION_STATION,
            "destinationStation.equals=" + UPDATED_DESTINATION_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDestinationStationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where destinationStation in
        defaultBookingFiltering(
            "destinationStation.in=" + DEFAULT_DESTINATION_STATION + "," + UPDATED_DESTINATION_STATION,
            "destinationStation.in=" + UPDATED_DESTINATION_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDestinationStationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where destinationStation is not null
        defaultBookingFiltering("destinationStation.specified=true", "destinationStation.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByDestinationStationContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where destinationStation contains
        defaultBookingFiltering(
            "destinationStation.contains=" + DEFAULT_DESTINATION_STATION,
            "destinationStation.contains=" + UPDATED_DESTINATION_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDestinationStationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where destinationStation does not contain
        defaultBookingFiltering(
            "destinationStation.doesNotContain=" + UPDATED_DESTINATION_STATION,
            "destinationStation.doesNotContain=" + DEFAULT_DESTINATION_STATION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingTime equals to
        defaultBookingFiltering("boardingTime.equals=" + DEFAULT_BOARDING_TIME, "boardingTime.equals=" + UPDATED_BOARDING_TIME);
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingTime in
        defaultBookingFiltering(
            "boardingTime.in=" + DEFAULT_BOARDING_TIME + "," + UPDATED_BOARDING_TIME,
            "boardingTime.in=" + UPDATED_BOARDING_TIME
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBoardingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where boardingTime is not null
        defaultBookingFiltering("boardingTime.specified=true", "boardingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByArrivalTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where arrivalTime equals to
        defaultBookingFiltering("arrivalTime.equals=" + DEFAULT_ARRIVAL_TIME, "arrivalTime.equals=" + UPDATED_ARRIVAL_TIME);
    }

    @Test
    @Transactional
    void getAllBookingsByArrivalTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where arrivalTime in
        defaultBookingFiltering(
            "arrivalTime.in=" + DEFAULT_ARRIVAL_TIME + "," + UPDATED_ARRIVAL_TIME,
            "arrivalTime.in=" + UPDATED_ARRIVAL_TIME
        );
    }

    @Test
    @Transactional
    void getAllBookingsByArrivalTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where arrivalTime is not null
        defaultBookingFiltering("arrivalTime.specified=true", "arrivalTime.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare equals to
        defaultBookingFiltering("totalFare.equals=" + DEFAULT_TOTAL_FARE, "totalFare.equals=" + UPDATED_TOTAL_FARE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare in
        defaultBookingFiltering("totalFare.in=" + DEFAULT_TOTAL_FARE + "," + UPDATED_TOTAL_FARE, "totalFare.in=" + UPDATED_TOTAL_FARE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare is not null
        defaultBookingFiltering("totalFare.specified=true", "totalFare.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare is greater than or equal to
        defaultBookingFiltering("totalFare.greaterThanOrEqual=" + DEFAULT_TOTAL_FARE, "totalFare.greaterThanOrEqual=" + UPDATED_TOTAL_FARE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare is less than or equal to
        defaultBookingFiltering("totalFare.lessThanOrEqual=" + DEFAULT_TOTAL_FARE, "totalFare.lessThanOrEqual=" + SMALLER_TOTAL_FARE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare is less than
        defaultBookingFiltering("totalFare.lessThan=" + UPDATED_TOTAL_FARE, "totalFare.lessThan=" + DEFAULT_TOTAL_FARE);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalFareIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalFare is greater than
        defaultBookingFiltering("totalFare.greaterThan=" + SMALLER_TOTAL_FARE, "totalFare.greaterThan=" + DEFAULT_TOTAL_FARE);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingStatus equals to
        defaultBookingFiltering("bookingStatus.equals=" + DEFAULT_BOOKING_STATUS, "bookingStatus.equals=" + UPDATED_BOOKING_STATUS);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingStatus in
        defaultBookingFiltering(
            "bookingStatus.in=" + DEFAULT_BOOKING_STATUS + "," + UPDATED_BOOKING_STATUS,
            "bookingStatus.in=" + UPDATED_BOOKING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllBookingsByBookingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingStatus is not null
        defaultBookingFiltering("bookingStatus.specified=true", "bookingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByModeOfPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where modeOfPayment equals to
        defaultBookingFiltering("modeOfPayment.equals=" + DEFAULT_MODE_OF_PAYMENT, "modeOfPayment.equals=" + UPDATED_MODE_OF_PAYMENT);
    }

    @Test
    @Transactional
    void getAllBookingsByModeOfPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where modeOfPayment in
        defaultBookingFiltering(
            "modeOfPayment.in=" + DEFAULT_MODE_OF_PAYMENT + "," + UPDATED_MODE_OF_PAYMENT,
            "modeOfPayment.in=" + UPDATED_MODE_OF_PAYMENT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByModeOfPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where modeOfPayment is not null
        defaultBookingFiltering("modeOfPayment.specified=true", "modeOfPayment.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByAdditionalServicesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where additionalServices equals to
        defaultBookingFiltering(
            "additionalServices.equals=" + DEFAULT_ADDITIONAL_SERVICES,
            "additionalServices.equals=" + UPDATED_ADDITIONAL_SERVICES
        );
    }

    @Test
    @Transactional
    void getAllBookingsByAdditionalServicesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where additionalServices in
        defaultBookingFiltering(
            "additionalServices.in=" + DEFAULT_ADDITIONAL_SERVICES + "," + UPDATED_ADDITIONAL_SERVICES,
            "additionalServices.in=" + UPDATED_ADDITIONAL_SERVICES
        );
    }

    @Test
    @Transactional
    void getAllBookingsByAdditionalServicesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where additionalServices is not null
        defaultBookingFiltering("additionalServices.specified=true", "additionalServices.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCoachNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where coachNumber equals to
        defaultBookingFiltering("coachNumber.equals=" + DEFAULT_COACH_NUMBER, "coachNumber.equals=" + UPDATED_COACH_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByCoachNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where coachNumber in
        defaultBookingFiltering(
            "coachNumber.in=" + DEFAULT_COACH_NUMBER + "," + UPDATED_COACH_NUMBER,
            "coachNumber.in=" + UPDATED_COACH_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCoachNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where coachNumber is not null
        defaultBookingFiltering("coachNumber.specified=true", "coachNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCoachNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where coachNumber contains
        defaultBookingFiltering("coachNumber.contains=" + DEFAULT_COACH_NUMBER, "coachNumber.contains=" + UPDATED_COACH_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByCoachNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where coachNumber does not contain
        defaultBookingFiltering("coachNumber.doesNotContain=" + UPDATED_COACH_NUMBER, "coachNumber.doesNotContain=" + DEFAULT_COACH_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsBySeatNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where seatNumber equals to
        defaultBookingFiltering("seatNumber.equals=" + DEFAULT_SEAT_NUMBER, "seatNumber.equals=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsBySeatNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where seatNumber in
        defaultBookingFiltering("seatNumber.in=" + DEFAULT_SEAT_NUMBER + "," + UPDATED_SEAT_NUMBER, "seatNumber.in=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsBySeatNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where seatNumber is not null
        defaultBookingFiltering("seatNumber.specified=true", "seatNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsBySeatNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where seatNumber contains
        defaultBookingFiltering("seatNumber.contains=" + DEFAULT_SEAT_NUMBER, "seatNumber.contains=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsBySeatNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where seatNumber does not contain
        defaultBookingFiltering("seatNumber.doesNotContain=" + UPDATED_SEAT_NUMBER, "seatNumber.doesNotContain=" + DEFAULT_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            customer = CustomerResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        booking.setCustomer(customer);
        bookingRepository.saveAndFlush(booking);
        Long customerId = customer.getId();
        // Get all the bookingList where customer equals to customerId
        defaultBookingShouldBeFound("customerId.equals=" + customerId);

        // Get all the bookingList where customer equals to (customerId + 1)
        defaultBookingShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByTrainIsEqualToSomething() throws Exception {
        Train train;
        if (TestUtil.findAll(em, Train.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            train = TrainResourceIT.createEntity();
        } else {
            train = TestUtil.findAll(em, Train.class).get(0);
        }
        em.persist(train);
        em.flush();
        booking.setTrain(train);
        bookingRepository.saveAndFlush(booking);
        Long trainId = train.getId();
        // Get all the bookingList where train equals to trainId
        defaultBookingShouldBeFound("trainId.equals=" + trainId);

        // Get all the bookingList where train equals to (trainId + 1)
        defaultBookingShouldNotBeFound("trainId.equals=" + (trainId + 1));
    }

    private void defaultBookingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBookingShouldBeFound(shouldBeFound);
        defaultBookingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].pnrNumber").value(hasItem(DEFAULT_PNR_NUMBER)))
            .andExpect(jsonPath("$.[*].bookingDate").value(hasItem(DEFAULT_BOOKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].travellingDate").value(hasItem(DEFAULT_TRAVELLING_DATE.toString())))
            .andExpect(jsonPath("$.[*].boardingStation").value(hasItem(DEFAULT_BOARDING_STATION)))
            .andExpect(jsonPath("$.[*].destinationStation").value(hasItem(DEFAULT_DESTINATION_STATION)))
            .andExpect(jsonPath("$.[*].boardingTime").value(hasItem(DEFAULT_BOARDING_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalTime").value(hasItem(DEFAULT_ARRIVAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].totalFare").value(hasItem(sameNumber(DEFAULT_TOTAL_FARE))))
            .andExpect(jsonPath("$.[*].bookingStatus").value(hasItem(DEFAULT_BOOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modeOfPayment").value(hasItem(DEFAULT_MODE_OF_PAYMENT.toString())))
            .andExpect(jsonPath("$.[*].additionalServices").value(hasItem(DEFAULT_ADDITIONAL_SERVICES)))
            .andExpect(jsonPath("$.[*].coachNumber").value(hasItem(DEFAULT_COACH_NUMBER)))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)));

        // Check, that the count call also returns 1
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooking() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .pnrNumber(UPDATED_PNR_NUMBER)
            .bookingDate(UPDATED_BOOKING_DATE)
            .travellingDate(UPDATED_TRAVELLING_DATE)
            .boardingStation(UPDATED_BOARDING_STATION)
            .destinationStation(UPDATED_DESTINATION_STATION)
            .boardingTime(UPDATED_BOARDING_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .totalFare(UPDATED_TOTAL_FARE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .additionalServices(UPDATED_ADDITIONAL_SERVICES)
            .coachNumber(UPDATED_COACH_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingToMatchAllProperties(updatedBooking);
    }

    @Test
    @Transactional
    void putNonExistingBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setId(booking.getId());

        partialUpdatedBooking
            .pnrNumber(UPDATED_PNR_NUMBER)
            .bookingDate(UPDATED_BOOKING_DATE)
            .destinationStation(UPDATED_DESTINATION_STATION)
            .boardingTime(UPDATED_BOARDING_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .coachNumber(UPDATED_COACH_NUMBER);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBooking, booking), getPersistedBooking(booking));
    }

    @Test
    @Transactional
    void fullUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setId(booking.getId());

        partialUpdatedBooking
            .pnrNumber(UPDATED_PNR_NUMBER)
            .bookingDate(UPDATED_BOOKING_DATE)
            .travellingDate(UPDATED_TRAVELLING_DATE)
            .boardingStation(UPDATED_BOARDING_STATION)
            .destinationStation(UPDATED_DESTINATION_STATION)
            .boardingTime(UPDATED_BOARDING_TIME)
            .arrivalTime(UPDATED_ARRIVAL_TIME)
            .totalFare(UPDATED_TOTAL_FARE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .additionalServices(UPDATED_ADDITIONAL_SERVICES)
            .coachNumber(UPDATED_COACH_NUMBER)
            .seatNumber(UPDATED_SEAT_NUMBER);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingUpdatableFieldsEquals(partialUpdatedBooking, getPersistedBooking(partialUpdatedBooking));
    }

    @Test
    @Transactional
    void patchNonExistingBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooking() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the booking
        restBookingMockMvc
            .perform(delete(ENTITY_API_URL_ID, booking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingRepository.count();
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

    protected Booking getPersistedBooking(Booking booking) {
        return bookingRepository.findById(booking.getId()).orElseThrow();
    }

    protected void assertPersistedBookingToMatchAllProperties(Booking expectedBooking) {
        assertBookingAllPropertiesEquals(expectedBooking, getPersistedBooking(expectedBooking));
    }

    protected void assertPersistedBookingToMatchUpdatableProperties(Booking expectedBooking) {
        assertBookingAllUpdatablePropertiesEquals(expectedBooking, getPersistedBooking(expectedBooking));
    }
}
