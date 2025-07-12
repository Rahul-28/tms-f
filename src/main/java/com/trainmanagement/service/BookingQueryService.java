package com.trainmanagement.service;

import com.trainmanagement.domain.*; // for static metamodels
import com.trainmanagement.domain.Booking;
import com.trainmanagement.repository.BookingRepository;
import com.trainmanagement.service.criteria.BookingCriteria;
import com.trainmanagement.service.dto.BookingDTO;
import com.trainmanagement.service.mapper.BookingMapper;
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
 * Service for executing complex queries for {@link Booking} entities in the database.
 * The main input is a {@link BookingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BookingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookingQueryService extends QueryService<Booking> {

    private static final Logger LOG = LoggerFactory.getLogger(BookingQueryService.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingQueryService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Return a {@link Page} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findByCriteria(BookingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page).map(bookingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.count(specification);
    }

    /**
     * Function to convert {@link BookingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Booking> createSpecification(BookingCriteria criteria) {
        Specification<Booking> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Booking_.id),
                buildStringSpecification(criteria.getPnrNumber(), Booking_.pnrNumber),
                buildRangeSpecification(criteria.getBookingDate(), Booking_.bookingDate),
                buildRangeSpecification(criteria.getTravellingDate(), Booking_.travellingDate),
                buildStringSpecification(criteria.getBoardingStation(), Booking_.boardingStation),
                buildStringSpecification(criteria.getDestinationStation(), Booking_.destinationStation),
                buildRangeSpecification(criteria.getBoardingTime(), Booking_.boardingTime),
                buildRangeSpecification(criteria.getArrivalTime(), Booking_.arrivalTime),
                buildRangeSpecification(criteria.getTotalFare(), Booking_.totalFare),
                buildSpecification(criteria.getBookingStatus(), Booking_.bookingStatus),
                buildSpecification(criteria.getModeOfPayment(), Booking_.modeOfPayment),
                buildSpecification(criteria.getAdditionalServices(), Booking_.additionalServices),
                buildStringSpecification(criteria.getCoachNumber(), Booking_.coachNumber),
                buildStringSpecification(criteria.getSeatNumber(), Booking_.seatNumber),
                buildSpecification(criteria.getPassengersId(), root -> root.join(Booking_.passengers, JoinType.LEFT).get(Passenger_.id)),
                buildSpecification(criteria.getPaymentId(), root -> root.join(Booking_.payments, JoinType.LEFT).get(Payment_.id)),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Booking_.customer, JoinType.LEFT).get(Customer_.id)),
                buildSpecification(criteria.getTrainId(), root -> root.join(Booking_.train, JoinType.LEFT).get(Train_.id))
            );
        }
        return specification;
    }
}
