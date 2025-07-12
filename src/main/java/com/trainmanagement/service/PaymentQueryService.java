package com.trainmanagement.service;

import com.trainmanagement.domain.*; // for static metamodels
import com.trainmanagement.domain.Payment;
import com.trainmanagement.repository.PaymentRepository;
import com.trainmanagement.service.criteria.PaymentCriteria;
import com.trainmanagement.service.dto.PaymentDTO;
import com.trainmanagement.service.mapper.PaymentMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Payment} entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentQueryService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Return a {@link List} of {@link PaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByCriteria(PaymentCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentMapper.toDto(paymentRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Payment_.id),
                buildStringSpecification(criteria.getPaymentId(), Payment_.paymentId),
                buildStringSpecification(criteria.getTransactionId(), Payment_.transactionId),
                buildStringSpecification(criteria.getReceiptNumber(), Payment_.receiptNumber),
                buildRangeSpecification(criteria.getTransactionDate(), Payment_.transactionDate),
                buildSpecification(criteria.getTransactionType(), Payment_.transactionType),
                buildRangeSpecification(criteria.getTransactionAmount(), Payment_.transactionAmount),
                buildSpecification(criteria.getTransactionStatus(), Payment_.transactionStatus),
                buildStringSpecification(criteria.getCardNumber(), Payment_.cardNumber),
                buildStringSpecification(criteria.getExpiryDate(), Payment_.expiryDate),
                buildStringSpecification(criteria.getCvv(), Payment_.cvv),
                buildStringSpecification(criteria.getCardholderName(), Payment_.cardholderName),
                buildSpecification(criteria.getInvoicesId(), root -> root.join(Payment_.invoices, JoinType.LEFT).get(Invoice_.id)),
                buildSpecification(criteria.getBookingId(), root -> root.join(Payment_.booking, JoinType.LEFT).get(Booking_.id))
            );
        }
        return specification;
    }
}
