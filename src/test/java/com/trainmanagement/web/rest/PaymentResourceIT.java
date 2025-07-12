package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.PaymentAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static com.trainmanagement.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.Booking;
import com.trainmanagement.domain.Payment;
import com.trainmanagement.domain.enumeration.TransactionStatus;
import com.trainmanagement.domain.enumeration.TransactionType;
import com.trainmanagement.repository.PaymentRepository;
import com.trainmanagement.service.dto.PaymentDTO;
import com.trainmanagement.service.mapper.PaymentMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final String DEFAULT_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIPT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.CREDIT;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.DEBIT;

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT = new BigDecimal(1 - 1);

    private static final TransactionStatus DEFAULT_TRANSACTION_STATUS = TransactionStatus.SUCCESS;
    private static final TransactionStatus UPDATED_TRANSACTION_STATUS = TransactionStatus.FAILED;

    private static final String DEFAULT_CARD_NUMBER = "3420431282635333";
    private static final String UPDATED_CARD_NUMBER = "4644676684113155";

    private static final String DEFAULT_EXPIRY_DATE = "AAAAAAAAAA";
    private static final String UPDATED_EXPIRY_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_CVV = "337";
    private static final String UPDATED_CVV = "948";

    private static final String DEFAULT_CARDHOLDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CARDHOLDER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    private Payment insertedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity() {
        return new Payment()
            .paymentId(DEFAULT_PAYMENT_ID)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .receiptNumber(DEFAULT_RECEIPT_NUMBER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .transactionAmount(DEFAULT_TRANSACTION_AMOUNT)
            .transactionStatus(DEFAULT_TRANSACTION_STATUS)
            .cardNumber(DEFAULT_CARD_NUMBER)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .cvv(DEFAULT_CVV)
            .cardholderName(DEFAULT_CARDHOLDER_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity() {
        return new Payment()
            .paymentId(UPDATED_PAYMENT_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .cardNumber(UPDATED_CARD_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .cvv(UPDATED_CVV)
            .cardholderName(UPDATED_CARDHOLDER_NAME);
    }

    @BeforeEach
    void initTest() {
        payment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPayment != null) {
            paymentRepository.delete(insertedPayment);
            insertedPayment = null;
        }
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        var returnedPaymentDTO = om.readValue(
            restPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentDTO.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPayment = paymentMapper.toEntity(returnedPaymentDTO);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));

        insertedPayment = returnedPayment;
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPaymentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setPaymentId(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setTransactionId(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceiptNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setReceiptNumber(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setTransactionDate(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setTransactionType(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setTransactionAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setTransactionStatus(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].receiptNumber").value(hasItem(DEFAULT_RECEIPT_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT))))
            .andExpect(jsonPath("$.[*].transactionStatus").value(hasItem(DEFAULT_TRANSACTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE)))
            .andExpect(jsonPath("$.[*].cvv").value(hasItem(DEFAULT_CVV)))
            .andExpect(jsonPath("$.[*].cardholderName").value(hasItem(DEFAULT_CARDHOLDER_NAME)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.receiptNumber").value(DEFAULT_RECEIPT_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.transactionAmount").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT)))
            .andExpect(jsonPath("$.transactionStatus").value(DEFAULT_TRANSACTION_STATUS.toString()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE))
            .andExpect(jsonPath("$.cvv").value(DEFAULT_CVV))
            .andExpect(jsonPath("$.cardholderName").value(DEFAULT_CARDHOLDER_NAME));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentId equals to
        defaultPaymentFiltering("paymentId.equals=" + DEFAULT_PAYMENT_ID, "paymentId.equals=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentId in
        defaultPaymentFiltering("paymentId.in=" + DEFAULT_PAYMENT_ID + "," + UPDATED_PAYMENT_ID, "paymentId.in=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentId is not null
        defaultPaymentFiltering("paymentId.specified=true", "paymentId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentId contains
        defaultPaymentFiltering("paymentId.contains=" + DEFAULT_PAYMENT_ID, "paymentId.contains=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentId does not contain
        defaultPaymentFiltering("paymentId.doesNotContain=" + UPDATED_PAYMENT_ID, "paymentId.doesNotContain=" + DEFAULT_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId equals to
        defaultPaymentFiltering("transactionId.equals=" + DEFAULT_TRANSACTION_ID, "transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId in
        defaultPaymentFiltering(
            "transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID,
            "transactionId.in=" + UPDATED_TRANSACTION_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId is not null
        defaultPaymentFiltering("transactionId.specified=true", "transactionId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId contains
        defaultPaymentFiltering("transactionId.contains=" + DEFAULT_TRANSACTION_ID, "transactionId.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId does not contain
        defaultPaymentFiltering(
            "transactionId.doesNotContain=" + UPDATED_TRANSACTION_ID,
            "transactionId.doesNotContain=" + DEFAULT_TRANSACTION_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByReceiptNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where receiptNumber equals to
        defaultPaymentFiltering("receiptNumber.equals=" + DEFAULT_RECEIPT_NUMBER, "receiptNumber.equals=" + UPDATED_RECEIPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByReceiptNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where receiptNumber in
        defaultPaymentFiltering(
            "receiptNumber.in=" + DEFAULT_RECEIPT_NUMBER + "," + UPDATED_RECEIPT_NUMBER,
            "receiptNumber.in=" + UPDATED_RECEIPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByReceiptNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where receiptNumber is not null
        defaultPaymentFiltering("receiptNumber.specified=true", "receiptNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByReceiptNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where receiptNumber contains
        defaultPaymentFiltering("receiptNumber.contains=" + DEFAULT_RECEIPT_NUMBER, "receiptNumber.contains=" + UPDATED_RECEIPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByReceiptNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where receiptNumber does not contain
        defaultPaymentFiltering(
            "receiptNumber.doesNotContain=" + UPDATED_RECEIPT_NUMBER,
            "receiptNumber.doesNotContain=" + DEFAULT_RECEIPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionDate equals to
        defaultPaymentFiltering("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE, "transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionDate in
        defaultPaymentFiltering(
            "transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE,
            "transactionDate.in=" + UPDATED_TRANSACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionDate is not null
        defaultPaymentFiltering("transactionDate.specified=true", "transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionType equals to
        defaultPaymentFiltering("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE, "transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionType in
        defaultPaymentFiltering(
            "transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE,
            "transactionType.in=" + UPDATED_TRANSACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionType is not null
        defaultPaymentFiltering("transactionType.specified=true", "transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount equals to
        defaultPaymentFiltering(
            "transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT,
            "transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount in
        defaultPaymentFiltering(
            "transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT,
            "transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount is not null
        defaultPaymentFiltering("transactionAmount.specified=true", "transactionAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount is greater than or equal to
        defaultPaymentFiltering(
            "transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT,
            "transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount is less than or equal to
        defaultPaymentFiltering(
            "transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT,
            "transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount is less than
        defaultPaymentFiltering(
            "transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT,
            "transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionAmount is greater than
        defaultPaymentFiltering(
            "transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT,
            "transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionStatus equals to
        defaultPaymentFiltering(
            "transactionStatus.equals=" + DEFAULT_TRANSACTION_STATUS,
            "transactionStatus.equals=" + UPDATED_TRANSACTION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionStatus in
        defaultPaymentFiltering(
            "transactionStatus.in=" + DEFAULT_TRANSACTION_STATUS + "," + UPDATED_TRANSACTION_STATUS,
            "transactionStatus.in=" + UPDATED_TRANSACTION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionStatus is not null
        defaultPaymentFiltering("transactionStatus.specified=true", "transactionStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCardNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardNumber equals to
        defaultPaymentFiltering("cardNumber.equals=" + DEFAULT_CARD_NUMBER, "cardNumber.equals=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByCardNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardNumber in
        defaultPaymentFiltering("cardNumber.in=" + DEFAULT_CARD_NUMBER + "," + UPDATED_CARD_NUMBER, "cardNumber.in=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByCardNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardNumber is not null
        defaultPaymentFiltering("cardNumber.specified=true", "cardNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCardNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardNumber contains
        defaultPaymentFiltering("cardNumber.contains=" + DEFAULT_CARD_NUMBER, "cardNumber.contains=" + UPDATED_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByCardNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardNumber does not contain
        defaultPaymentFiltering("cardNumber.doesNotContain=" + UPDATED_CARD_NUMBER, "cardNumber.doesNotContain=" + DEFAULT_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where expiryDate equals to
        defaultPaymentFiltering("expiryDate.equals=" + DEFAULT_EXPIRY_DATE, "expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where expiryDate in
        defaultPaymentFiltering("expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE, "expiryDate.in=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where expiryDate is not null
        defaultPaymentFiltering("expiryDate.specified=true", "expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByExpiryDateContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where expiryDate contains
        defaultPaymentFiltering("expiryDate.contains=" + DEFAULT_EXPIRY_DATE, "expiryDate.contains=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByExpiryDateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where expiryDate does not contain
        defaultPaymentFiltering("expiryDate.doesNotContain=" + UPDATED_EXPIRY_DATE, "expiryDate.doesNotContain=" + DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByCvvIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cvv equals to
        defaultPaymentFiltering("cvv.equals=" + DEFAULT_CVV, "cvv.equals=" + UPDATED_CVV);
    }

    @Test
    @Transactional
    void getAllPaymentsByCvvIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cvv in
        defaultPaymentFiltering("cvv.in=" + DEFAULT_CVV + "," + UPDATED_CVV, "cvv.in=" + UPDATED_CVV);
    }

    @Test
    @Transactional
    void getAllPaymentsByCvvIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cvv is not null
        defaultPaymentFiltering("cvv.specified=true", "cvv.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCvvContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cvv contains
        defaultPaymentFiltering("cvv.contains=" + DEFAULT_CVV, "cvv.contains=" + UPDATED_CVV);
    }

    @Test
    @Transactional
    void getAllPaymentsByCvvNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cvv does not contain
        defaultPaymentFiltering("cvv.doesNotContain=" + UPDATED_CVV, "cvv.doesNotContain=" + DEFAULT_CVV);
    }

    @Test
    @Transactional
    void getAllPaymentsByCardholderNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardholderName equals to
        defaultPaymentFiltering("cardholderName.equals=" + DEFAULT_CARDHOLDER_NAME, "cardholderName.equals=" + UPDATED_CARDHOLDER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByCardholderNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardholderName in
        defaultPaymentFiltering(
            "cardholderName.in=" + DEFAULT_CARDHOLDER_NAME + "," + UPDATED_CARDHOLDER_NAME,
            "cardholderName.in=" + UPDATED_CARDHOLDER_NAME
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByCardholderNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardholderName is not null
        defaultPaymentFiltering("cardholderName.specified=true", "cardholderName.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCardholderNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardholderName contains
        defaultPaymentFiltering("cardholderName.contains=" + DEFAULT_CARDHOLDER_NAME, "cardholderName.contains=" + UPDATED_CARDHOLDER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByCardholderNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where cardholderName does not contain
        defaultPaymentFiltering(
            "cardholderName.doesNotContain=" + UPDATED_CARDHOLDER_NAME,
            "cardholderName.doesNotContain=" + DEFAULT_CARDHOLDER_NAME
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByBookingIsEqualToSomething() throws Exception {
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            booking = BookingResourceIT.createEntity();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        em.persist(booking);
        em.flush();
        payment.setBooking(booking);
        paymentRepository.saveAndFlush(payment);
        Long bookingId = booking.getId();
        // Get all the paymentList where booking equals to bookingId
        defaultPaymentShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the paymentList where booking equals to (bookingId + 1)
        defaultPaymentShouldNotBeFound("bookingId.equals=" + (bookingId + 1));
    }

    private void defaultPaymentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentShouldBeFound(shouldBeFound);
        defaultPaymentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].receiptNumber").value(hasItem(DEFAULT_RECEIPT_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT))))
            .andExpect(jsonPath("$.[*].transactionStatus").value(hasItem(DEFAULT_TRANSACTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE)))
            .andExpect(jsonPath("$.[*].cvv").value(hasItem(DEFAULT_CVV)))
            .andExpect(jsonPath("$.[*].cardholderName").value(hasItem(DEFAULT_CARDHOLDER_NAME)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .paymentId(UPDATED_PAYMENT_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .cardNumber(UPDATED_CARD_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .cvv(UPDATED_CVV)
            .cardholderName(UPDATED_CARDHOLDER_NAME);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .paymentId(UPDATED_PAYMENT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .cardNumber(UPDATED_CARD_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .cardholderName(UPDATED_CARDHOLDER_NAME);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .paymentId(UPDATED_PAYMENT_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .cardNumber(UPDATED_CARD_NUMBER)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .cvv(UPDATED_CVV)
            .cardholderName(UPDATED_CARDHOLDER_NAME);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
