package com.trainmanagement.web.rest;

import static com.trainmanagement.domain.CustomerAsserts.*;
import static com.trainmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainmanagement.IntegrationTest;
import com.trainmanagement.domain.Customer;
import com.trainmanagement.repository.CustomerRepository;
import com.trainmanagement.service.dto.CustomerDTO;
import com.trainmanagement.service.mapper.CustomerMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "kyaYr@q'.3]qJ-";
    private static final String UPDATED_EMAIL = "Q@5i<&Y.8N0|";

    private static final String DEFAULT_MOBILE_NUMBER = "7919879544";
    private static final String UPDATED_MOBILE_NUMBER = "6593201042";

    private static final String DEFAULT_AADHAAR_NUMBER = "778729516953";
    private static final String UPDATED_AADHAAR_NUMBER = "532303364576";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_INFORMATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_REGISTRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    private Customer insertedCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity() {
        return new Customer()
            .customerId(DEFAULT_CUSTOMER_ID)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .aadhaarNumber(DEFAULT_AADHAAR_NUMBER)
            .address(DEFAULT_ADDRESS)
            .contactInformation(DEFAULT_CONTACT_INFORMATION)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity() {
        return new Customer()
            .customerId(UPDATED_CUSTOMER_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .aadhaarNumber(UPDATED_AADHAAR_NUMBER)
            .address(UPDATED_ADDRESS)
            .contactInformation(UPDATED_CONTACT_INFORMATION)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        customer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomer != null) {
            customerRepository.delete(insertedCustomer);
            insertedCustomer = null;
        }
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        var returnedCustomerDTO = om.readValue(
            restCustomerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerDTO.class
        );

        // Validate the Customer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomer = customerMapper.toEntity(returnedCustomerDTO);
        assertCustomerUpdatableFieldsEquals(returnedCustomer, getPersistedCustomer(returnedCustomer));

        insertedCustomer = returnedCustomer;
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setCustomerId(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setUsername(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setPassword(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setEmail(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setMobileNumber(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAadhaarNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setAadhaarNumber(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customer.setIsActive(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].aadhaarNumber").value(hasItem(DEFAULT_AADHAAR_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactInformation").value(hasItem(DEFAULT_CONTACT_INFORMATION)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.aadhaarNumber").value(DEFAULT_AADHAAR_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.contactInformation").value(DEFAULT_CONTACT_INFORMATION))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerId equals to
        defaultCustomerFiltering("customerId.equals=" + DEFAULT_CUSTOMER_ID, "customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerId in
        defaultCustomerFiltering(
            "customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID,
            "customerId.in=" + UPDATED_CUSTOMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerId is not null
        defaultCustomerFiltering("customerId.specified=true", "customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerId contains
        defaultCustomerFiltering("customerId.contains=" + DEFAULT_CUSTOMER_ID, "customerId.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerId does not contain
        defaultCustomerFiltering("customerId.doesNotContain=" + UPDATED_CUSTOMER_ID, "customerId.doesNotContain=" + DEFAULT_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where username equals to
        defaultCustomerFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where username in
        defaultCustomerFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where username is not null
        defaultCustomerFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where username contains
        defaultCustomerFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where username does not contain
        defaultCustomerFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where password equals to
        defaultCustomerFiltering("password.equals=" + DEFAULT_PASSWORD, "password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllCustomersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where password in
        defaultCustomerFiltering("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD, "password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllCustomersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where password is not null
        defaultCustomerFiltering("password.specified=true", "password.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where password contains
        defaultCustomerFiltering("password.contains=" + DEFAULT_PASSWORD, "password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllCustomersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where password does not contain
        defaultCustomerFiltering("password.doesNotContain=" + UPDATED_PASSWORD, "password.doesNotContain=" + DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where email equals to
        defaultCustomerFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where email in
        defaultCustomerFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where email is not null
        defaultCustomerFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where email contains
        defaultCustomerFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where email does not contain
        defaultCustomerFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNumber equals to
        defaultCustomerFiltering("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER, "mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNumber in
        defaultCustomerFiltering(
            "mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER,
            "mobileNumber.in=" + UPDATED_MOBILE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNumber is not null
        defaultCustomerFiltering("mobileNumber.specified=true", "mobileNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNumber contains
        defaultCustomerFiltering("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER, "mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNumber does not contain
        defaultCustomerFiltering(
            "mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER,
            "mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByAadhaarNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where aadhaarNumber equals to
        defaultCustomerFiltering("aadhaarNumber.equals=" + DEFAULT_AADHAAR_NUMBER, "aadhaarNumber.equals=" + UPDATED_AADHAAR_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByAadhaarNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where aadhaarNumber in
        defaultCustomerFiltering(
            "aadhaarNumber.in=" + DEFAULT_AADHAAR_NUMBER + "," + UPDATED_AADHAAR_NUMBER,
            "aadhaarNumber.in=" + UPDATED_AADHAAR_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByAadhaarNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where aadhaarNumber is not null
        defaultCustomerFiltering("aadhaarNumber.specified=true", "aadhaarNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAadhaarNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where aadhaarNumber contains
        defaultCustomerFiltering("aadhaarNumber.contains=" + DEFAULT_AADHAAR_NUMBER, "aadhaarNumber.contains=" + UPDATED_AADHAAR_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByAadhaarNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where aadhaarNumber does not contain
        defaultCustomerFiltering(
            "aadhaarNumber.doesNotContain=" + UPDATED_AADHAAR_NUMBER,
            "aadhaarNumber.doesNotContain=" + DEFAULT_AADHAAR_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address equals to
        defaultCustomerFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address in
        defaultCustomerFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address is not null
        defaultCustomerFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address contains
        defaultCustomerFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where address does not contain
        defaultCustomerFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByContactInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactInformation equals to
        defaultCustomerFiltering(
            "contactInformation.equals=" + DEFAULT_CONTACT_INFORMATION,
            "contactInformation.equals=" + UPDATED_CONTACT_INFORMATION
        );
    }

    @Test
    @Transactional
    void getAllCustomersByContactInformationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactInformation in
        defaultCustomerFiltering(
            "contactInformation.in=" + DEFAULT_CONTACT_INFORMATION + "," + UPDATED_CONTACT_INFORMATION,
            "contactInformation.in=" + UPDATED_CONTACT_INFORMATION
        );
    }

    @Test
    @Transactional
    void getAllCustomersByContactInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactInformation is not null
        defaultCustomerFiltering("contactInformation.specified=true", "contactInformation.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByContactInformationContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactInformation contains
        defaultCustomerFiltering(
            "contactInformation.contains=" + DEFAULT_CONTACT_INFORMATION,
            "contactInformation.contains=" + UPDATED_CONTACT_INFORMATION
        );
    }

    @Test
    @Transactional
    void getAllCustomersByContactInformationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where contactInformation does not contain
        defaultCustomerFiltering(
            "contactInformation.doesNotContain=" + UPDATED_CONTACT_INFORMATION,
            "contactInformation.doesNotContain=" + DEFAULT_CONTACT_INFORMATION
        );
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationDate equals to
        defaultCustomerFiltering(
            "registrationDate.equals=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.equals=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationDate in
        defaultCustomerFiltering(
            "registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE,
            "registrationDate.in=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCustomersByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where registrationDate is not null
        defaultCustomerFiltering("registrationDate.specified=true", "registrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive equals to
        defaultCustomerFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCustomersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive in
        defaultCustomerFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCustomersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        // Get all the customerList where isActive is not null
        defaultCustomerFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultCustomerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerShouldBeFound(shouldBeFound);
        defaultCustomerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].aadhaarNumber").value(hasItem(DEFAULT_AADHAAR_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contactInformation").value(hasItem(DEFAULT_CONTACT_INFORMATION)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .customerId(UPDATED_CUSTOMER_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .aadhaarNumber(UPDATED_AADHAAR_NUMBER)
            .address(UPDATED_ADDRESS)
            .contactInformation(UPDATED_CONTACT_INFORMATION)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .isActive(UPDATED_IS_ACTIVE);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerToMatchAllProperties(updatedCustomer);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .aadhaarNumber(UPDATED_AADHAAR_NUMBER)
            .address(UPDATED_ADDRESS)
            .isActive(UPDATED_IS_ACTIVE);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCustomer, customer), getPersistedCustomer(customer));
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .customerId(UPDATED_CUSTOMER_ID)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .aadhaarNumber(UPDATED_AADHAAR_NUMBER)
            .address(UPDATED_ADDRESS)
            .contactInformation(UPDATED_CONTACT_INFORMATION)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerUpdatableFieldsEquals(partialUpdatedCustomer, getPersistedCustomer(partialUpdatedCustomer));
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customer.setId(longCount.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        insertedCustomer = customerRepository.saveAndFlush(customer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerRepository.count();
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

    protected Customer getPersistedCustomer(Customer customer) {
        return customerRepository.findById(customer.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerToMatchAllProperties(Customer expectedCustomer) {
        assertCustomerAllPropertiesEquals(expectedCustomer, getPersistedCustomer(expectedCustomer));
    }

    protected void assertPersistedCustomerToMatchUpdatableProperties(Customer expectedCustomer) {
        assertCustomerAllUpdatablePropertiesEquals(expectedCustomer, getPersistedCustomer(expectedCustomer));
    }
}
