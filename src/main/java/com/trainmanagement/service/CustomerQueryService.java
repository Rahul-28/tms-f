package com.trainmanagement.service;

import com.trainmanagement.domain.*; // for static metamodels
import com.trainmanagement.domain.Customer;
import com.trainmanagement.repository.CustomerRepository;
import com.trainmanagement.service.criteria.CustomerCriteria;
import com.trainmanagement.service.dto.CustomerDTO;
import com.trainmanagement.service.mapper.CustomerMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Customer} entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerQueryService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findByCriteria(CustomerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page).map(customerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Customer_.id),
                buildStringSpecification(criteria.getCustomerId(), Customer_.customerId),
                buildStringSpecification(criteria.getUsername(), Customer_.username),
                buildStringSpecification(criteria.getPassword(), Customer_.password),
                buildStringSpecification(criteria.getEmail(), Customer_.email),
                buildStringSpecification(criteria.getMobileNumber(), Customer_.mobileNumber),
                buildStringSpecification(criteria.getAadhaarNumber(), Customer_.aadhaarNumber),
                buildStringSpecification(criteria.getAddress(), Customer_.address),
                buildStringSpecification(criteria.getContactInformation(), Customer_.contactInformation),
                buildRangeSpecification(criteria.getRegistrationDate(), Customer_.registrationDate),
                buildSpecification(criteria.getIsActive(), Customer_.isActive),
                buildSpecification(criteria.getBookingsId(), root -> root.join(Customer_.bookings, JoinType.LEFT).get(Booking_.id))
            );
        }
        return specification;
    }
}
