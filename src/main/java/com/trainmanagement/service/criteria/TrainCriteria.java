package com.trainmanagement.service.criteria;

import com.trainmanagement.domain.enumeration.ServiceType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trainmanagement.domain.Train} entity. This class is used
 * in {@link com.trainmanagement.web.rest.TrainResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trains?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ServiceType
     */
    public static class ServiceTypeFilter extends Filter<ServiceType> {

        public ServiceTypeFilter() {}

        public ServiceTypeFilter(ServiceTypeFilter filter) {
            super(filter);
        }

        @Override
        public ServiceTypeFilter copy() {
            return new ServiceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter trainNumber;

    private StringFilter trainName;

    private StringFilter origin;

    private StringFilter destination;

    private StringFilter intermediateStop;

    private LocalDateFilter serviceStartDate;

    private LocalDateFilter serviceEndDate;

    private ServiceTypeFilter serviceType;

    private InstantFilter departureTime;

    private InstantFilter arrivalTime;

    private BigDecimalFilter basicPrice;

    private BooleanFilter isActive;

    private LongFilter bookingsId;

    private LongFilter trainCoachesId;

    private Boolean distinct;

    public TrainCriteria() {}

    public TrainCriteria(TrainCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.trainNumber = other.optionalTrainNumber().map(StringFilter::copy).orElse(null);
        this.trainName = other.optionalTrainName().map(StringFilter::copy).orElse(null);
        this.origin = other.optionalOrigin().map(StringFilter::copy).orElse(null);
        this.destination = other.optionalDestination().map(StringFilter::copy).orElse(null);
        this.intermediateStop = other.optionalIntermediateStop().map(StringFilter::copy).orElse(null);
        this.serviceStartDate = other.optionalServiceStartDate().map(LocalDateFilter::copy).orElse(null);
        this.serviceEndDate = other.optionalServiceEndDate().map(LocalDateFilter::copy).orElse(null);
        this.serviceType = other.optionalServiceType().map(ServiceTypeFilter::copy).orElse(null);
        this.departureTime = other.optionalDepartureTime().map(InstantFilter::copy).orElse(null);
        this.arrivalTime = other.optionalArrivalTime().map(InstantFilter::copy).orElse(null);
        this.basicPrice = other.optionalBasicPrice().map(BigDecimalFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.bookingsId = other.optionalBookingsId().map(LongFilter::copy).orElse(null);
        this.trainCoachesId = other.optionalTrainCoachesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrainCriteria copy() {
        return new TrainCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTrainNumber() {
        return trainNumber;
    }

    public Optional<StringFilter> optionalTrainNumber() {
        return Optional.ofNullable(trainNumber);
    }

    public StringFilter trainNumber() {
        if (trainNumber == null) {
            setTrainNumber(new StringFilter());
        }
        return trainNumber;
    }

    public void setTrainNumber(StringFilter trainNumber) {
        this.trainNumber = trainNumber;
    }

    public StringFilter getTrainName() {
        return trainName;
    }

    public Optional<StringFilter> optionalTrainName() {
        return Optional.ofNullable(trainName);
    }

    public StringFilter trainName() {
        if (trainName == null) {
            setTrainName(new StringFilter());
        }
        return trainName;
    }

    public void setTrainName(StringFilter trainName) {
        this.trainName = trainName;
    }

    public StringFilter getOrigin() {
        return origin;
    }

    public Optional<StringFilter> optionalOrigin() {
        return Optional.ofNullable(origin);
    }

    public StringFilter origin() {
        if (origin == null) {
            setOrigin(new StringFilter());
        }
        return origin;
    }

    public void setOrigin(StringFilter origin) {
        this.origin = origin;
    }

    public StringFilter getDestination() {
        return destination;
    }

    public Optional<StringFilter> optionalDestination() {
        return Optional.ofNullable(destination);
    }

    public StringFilter destination() {
        if (destination == null) {
            setDestination(new StringFilter());
        }
        return destination;
    }

    public void setDestination(StringFilter destination) {
        this.destination = destination;
    }

    public StringFilter getIntermediateStop() {
        return intermediateStop;
    }

    public Optional<StringFilter> optionalIntermediateStop() {
        return Optional.ofNullable(intermediateStop);
    }

    public StringFilter intermediateStop() {
        if (intermediateStop == null) {
            setIntermediateStop(new StringFilter());
        }
        return intermediateStop;
    }

    public void setIntermediateStop(StringFilter intermediateStop) {
        this.intermediateStop = intermediateStop;
    }

    public LocalDateFilter getServiceStartDate() {
        return serviceStartDate;
    }

    public Optional<LocalDateFilter> optionalServiceStartDate() {
        return Optional.ofNullable(serviceStartDate);
    }

    public LocalDateFilter serviceStartDate() {
        if (serviceStartDate == null) {
            setServiceStartDate(new LocalDateFilter());
        }
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDateFilter serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDateFilter getServiceEndDate() {
        return serviceEndDate;
    }

    public Optional<LocalDateFilter> optionalServiceEndDate() {
        return Optional.ofNullable(serviceEndDate);
    }

    public LocalDateFilter serviceEndDate() {
        if (serviceEndDate == null) {
            setServiceEndDate(new LocalDateFilter());
        }
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDateFilter serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public ServiceTypeFilter getServiceType() {
        return serviceType;
    }

    public Optional<ServiceTypeFilter> optionalServiceType() {
        return Optional.ofNullable(serviceType);
    }

    public ServiceTypeFilter serviceType() {
        if (serviceType == null) {
            setServiceType(new ServiceTypeFilter());
        }
        return serviceType;
    }

    public void setServiceType(ServiceTypeFilter serviceType) {
        this.serviceType = serviceType;
    }

    public InstantFilter getDepartureTime() {
        return departureTime;
    }

    public Optional<InstantFilter> optionalDepartureTime() {
        return Optional.ofNullable(departureTime);
    }

    public InstantFilter departureTime() {
        if (departureTime == null) {
            setDepartureTime(new InstantFilter());
        }
        return departureTime;
    }

    public void setDepartureTime(InstantFilter departureTime) {
        this.departureTime = departureTime;
    }

    public InstantFilter getArrivalTime() {
        return arrivalTime;
    }

    public Optional<InstantFilter> optionalArrivalTime() {
        return Optional.ofNullable(arrivalTime);
    }

    public InstantFilter arrivalTime() {
        if (arrivalTime == null) {
            setArrivalTime(new InstantFilter());
        }
        return arrivalTime;
    }

    public void setArrivalTime(InstantFilter arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimalFilter getBasicPrice() {
        return basicPrice;
    }

    public Optional<BigDecimalFilter> optionalBasicPrice() {
        return Optional.ofNullable(basicPrice);
    }

    public BigDecimalFilter basicPrice() {
        if (basicPrice == null) {
            setBasicPrice(new BigDecimalFilter());
        }
        return basicPrice;
    }

    public void setBasicPrice(BigDecimalFilter basicPrice) {
        this.basicPrice = basicPrice;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getBookingsId() {
        return bookingsId;
    }

    public Optional<LongFilter> optionalBookingsId() {
        return Optional.ofNullable(bookingsId);
    }

    public LongFilter bookingsId() {
        if (bookingsId == null) {
            setBookingsId(new LongFilter());
        }
        return bookingsId;
    }

    public void setBookingsId(LongFilter bookingsId) {
        this.bookingsId = bookingsId;
    }

    public LongFilter getTrainCoachesId() {
        return trainCoachesId;
    }

    public Optional<LongFilter> optionalTrainCoachesId() {
        return Optional.ofNullable(trainCoachesId);
    }

    public LongFilter trainCoachesId() {
        if (trainCoachesId == null) {
            setTrainCoachesId(new LongFilter());
        }
        return trainCoachesId;
    }

    public void setTrainCoachesId(LongFilter trainCoachesId) {
        this.trainCoachesId = trainCoachesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TrainCriteria that = (TrainCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(trainNumber, that.trainNumber) &&
            Objects.equals(trainName, that.trainName) &&
            Objects.equals(origin, that.origin) &&
            Objects.equals(destination, that.destination) &&
            Objects.equals(intermediateStop, that.intermediateStop) &&
            Objects.equals(serviceStartDate, that.serviceStartDate) &&
            Objects.equals(serviceEndDate, that.serviceEndDate) &&
            Objects.equals(serviceType, that.serviceType) &&
            Objects.equals(departureTime, that.departureTime) &&
            Objects.equals(arrivalTime, that.arrivalTime) &&
            Objects.equals(basicPrice, that.basicPrice) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(bookingsId, that.bookingsId) &&
            Objects.equals(trainCoachesId, that.trainCoachesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            trainNumber,
            trainName,
            origin,
            destination,
            intermediateStop,
            serviceStartDate,
            serviceEndDate,
            serviceType,
            departureTime,
            arrivalTime,
            basicPrice,
            isActive,
            bookingsId,
            trainCoachesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTrainNumber().map(f -> "trainNumber=" + f + ", ").orElse("") +
            optionalTrainName().map(f -> "trainName=" + f + ", ").orElse("") +
            optionalOrigin().map(f -> "origin=" + f + ", ").orElse("") +
            optionalDestination().map(f -> "destination=" + f + ", ").orElse("") +
            optionalIntermediateStop().map(f -> "intermediateStop=" + f + ", ").orElse("") +
            optionalServiceStartDate().map(f -> "serviceStartDate=" + f + ", ").orElse("") +
            optionalServiceEndDate().map(f -> "serviceEndDate=" + f + ", ").orElse("") +
            optionalServiceType().map(f -> "serviceType=" + f + ", ").orElse("") +
            optionalDepartureTime().map(f -> "departureTime=" + f + ", ").orElse("") +
            optionalArrivalTime().map(f -> "arrivalTime=" + f + ", ").orElse("") +
            optionalBasicPrice().map(f -> "basicPrice=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalBookingsId().map(f -> "bookingsId=" + f + ", ").orElse("") +
            optionalTrainCoachesId().map(f -> "trainCoachesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
