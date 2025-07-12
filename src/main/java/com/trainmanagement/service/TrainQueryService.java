package com.trainmanagement.service;

import com.trainmanagement.domain.*; // for static metamodels
import com.trainmanagement.domain.Train;
import com.trainmanagement.repository.TrainRepository;
import com.trainmanagement.service.criteria.TrainCriteria;
import com.trainmanagement.service.dto.TrainDTO;
import com.trainmanagement.service.mapper.TrainMapper;
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
 * Service for executing complex queries for {@link Train} entities in the database.
 * The main input is a {@link TrainCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrainDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainQueryService extends QueryService<Train> {

    private static final Logger LOG = LoggerFactory.getLogger(TrainQueryService.class);

    private final TrainRepository trainRepository;

    private final TrainMapper trainMapper;

    public TrainQueryService(TrainRepository trainRepository, TrainMapper trainMapper) {
        this.trainRepository = trainRepository;
        this.trainMapper = trainMapper;
    }

    /**
     * Return a {@link Page} of {@link TrainDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainDTO> findByCriteria(TrainCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Train> specification = createSpecification(criteria);
        return trainRepository.findAll(specification, page).map(trainMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrainCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Train> specification = createSpecification(criteria);
        return trainRepository.count(specification);
    }

    /**
     * Function to convert {@link TrainCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Train> createSpecification(TrainCriteria criteria) {
        Specification<Train> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Train_.id),
                buildStringSpecification(criteria.getTrainNumber(), Train_.trainNumber),
                buildStringSpecification(criteria.getTrainName(), Train_.trainName),
                buildStringSpecification(criteria.getOrigin(), Train_.origin),
                buildStringSpecification(criteria.getDestination(), Train_.destination),
                buildStringSpecification(criteria.getIntermediateStop(), Train_.intermediateStop),
                buildRangeSpecification(criteria.getServiceStartDate(), Train_.serviceStartDate),
                buildRangeSpecification(criteria.getServiceEndDate(), Train_.serviceEndDate),
                buildSpecification(criteria.getServiceType(), Train_.serviceType),
                buildRangeSpecification(criteria.getDepartureTime(), Train_.departureTime),
                buildRangeSpecification(criteria.getArrivalTime(), Train_.arrivalTime),
                buildRangeSpecification(criteria.getBasicPrice(), Train_.basicPrice),
                buildSpecification(criteria.getIsActive(), Train_.isActive),
                buildSpecification(criteria.getBookingsId(), root -> root.join(Train_.bookings, JoinType.LEFT).get(Booking_.id)),
                buildSpecification(criteria.getTrainCoachesId(), root -> root.join(Train_.trainCoaches, JoinType.LEFT).get(TrainCoach_.id))
            );
        }
        return specification;
    }
}
