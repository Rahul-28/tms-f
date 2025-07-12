package com.trainmanagement.service.criteria;

import com.trainmanagement.domain.enumeration.BookingStatus;
import com.trainmanagement.domain.enumeration.PaymentMode;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trainmanagement.domain.Booking} entity. This class is used
 * in {@link com.trainmanagement.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BookingStatus
     */
    public static class BookingStatusFilter extends Filter<BookingStatus> {

        public BookingStatusFilter() {}

        public BookingStatusFilter(BookingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingStatusFilter copy() {
            return new BookingStatusFilter(this);
        }
    }

    /**
     * Class for filtering PaymentMode
     */
    public static class PaymentModeFilter extends Filter<PaymentMode> {

        public PaymentModeFilter() {}

        public PaymentModeFilter(PaymentModeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentModeFilter copy() {
            return new PaymentModeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pnrNumber;

    private InstantFilter bookingDate;

    private LocalDateFilter travellingDate;

    private StringFilter boardingStation;

    private StringFilter destinationStation;

    private InstantFilter boardingTime;

    private InstantFilter arrivalTime;

    private BigDecimalFilter totalFare;

    private BookingStatusFilter bookingStatus;

    private PaymentModeFilter modeOfPayment;

    private BooleanFilter additionalServices;

    private StringFilter coachNumber;

    private StringFilter seatNumber;

    private LongFilter passengersId;

    private LongFilter paymentId;

    private LongFilter customerId;

    private LongFilter trainId;

    private Boolean distinct;

    public BookingCriteria() {}

    public BookingCriteria(BookingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.pnrNumber = other.optionalPnrNumber().map(StringFilter::copy).orElse(null);
        this.bookingDate = other.optionalBookingDate().map(InstantFilter::copy).orElse(null);
        this.travellingDate = other.optionalTravellingDate().map(LocalDateFilter::copy).orElse(null);
        this.boardingStation = other.optionalBoardingStation().map(StringFilter::copy).orElse(null);
        this.destinationStation = other.optionalDestinationStation().map(StringFilter::copy).orElse(null);
        this.boardingTime = other.optionalBoardingTime().map(InstantFilter::copy).orElse(null);
        this.arrivalTime = other.optionalArrivalTime().map(InstantFilter::copy).orElse(null);
        this.totalFare = other.optionalTotalFare().map(BigDecimalFilter::copy).orElse(null);
        this.bookingStatus = other.optionalBookingStatus().map(BookingStatusFilter::copy).orElse(null);
        this.modeOfPayment = other.optionalModeOfPayment().map(PaymentModeFilter::copy).orElse(null);
        this.additionalServices = other.optionalAdditionalServices().map(BooleanFilter::copy).orElse(null);
        this.coachNumber = other.optionalCoachNumber().map(StringFilter::copy).orElse(null);
        this.seatNumber = other.optionalSeatNumber().map(StringFilter::copy).orElse(null);
        this.passengersId = other.optionalPassengersId().map(LongFilter::copy).orElse(null);
        this.paymentId = other.optionalPaymentId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.trainId = other.optionalTrainId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
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

    public StringFilter getPnrNumber() {
        return pnrNumber;
    }

    public Optional<StringFilter> optionalPnrNumber() {
        return Optional.ofNullable(pnrNumber);
    }

    public StringFilter pnrNumber() {
        if (pnrNumber == null) {
            setPnrNumber(new StringFilter());
        }
        return pnrNumber;
    }

    public void setPnrNumber(StringFilter pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public InstantFilter getBookingDate() {
        return bookingDate;
    }

    public Optional<InstantFilter> optionalBookingDate() {
        return Optional.ofNullable(bookingDate);
    }

    public InstantFilter bookingDate() {
        if (bookingDate == null) {
            setBookingDate(new InstantFilter());
        }
        return bookingDate;
    }

    public void setBookingDate(InstantFilter bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDateFilter getTravellingDate() {
        return travellingDate;
    }

    public Optional<LocalDateFilter> optionalTravellingDate() {
        return Optional.ofNullable(travellingDate);
    }

    public LocalDateFilter travellingDate() {
        if (travellingDate == null) {
            setTravellingDate(new LocalDateFilter());
        }
        return travellingDate;
    }

    public void setTravellingDate(LocalDateFilter travellingDate) {
        this.travellingDate = travellingDate;
    }

    public StringFilter getBoardingStation() {
        return boardingStation;
    }

    public Optional<StringFilter> optionalBoardingStation() {
        return Optional.ofNullable(boardingStation);
    }

    public StringFilter boardingStation() {
        if (boardingStation == null) {
            setBoardingStation(new StringFilter());
        }
        return boardingStation;
    }

    public void setBoardingStation(StringFilter boardingStation) {
        this.boardingStation = boardingStation;
    }

    public StringFilter getDestinationStation() {
        return destinationStation;
    }

    public Optional<StringFilter> optionalDestinationStation() {
        return Optional.ofNullable(destinationStation);
    }

    public StringFilter destinationStation() {
        if (destinationStation == null) {
            setDestinationStation(new StringFilter());
        }
        return destinationStation;
    }

    public void setDestinationStation(StringFilter destinationStation) {
        this.destinationStation = destinationStation;
    }

    public InstantFilter getBoardingTime() {
        return boardingTime;
    }

    public Optional<InstantFilter> optionalBoardingTime() {
        return Optional.ofNullable(boardingTime);
    }

    public InstantFilter boardingTime() {
        if (boardingTime == null) {
            setBoardingTime(new InstantFilter());
        }
        return boardingTime;
    }

    public void setBoardingTime(InstantFilter boardingTime) {
        this.boardingTime = boardingTime;
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

    public BigDecimalFilter getTotalFare() {
        return totalFare;
    }

    public Optional<BigDecimalFilter> optionalTotalFare() {
        return Optional.ofNullable(totalFare);
    }

    public BigDecimalFilter totalFare() {
        if (totalFare == null) {
            setTotalFare(new BigDecimalFilter());
        }
        return totalFare;
    }

    public void setTotalFare(BigDecimalFilter totalFare) {
        this.totalFare = totalFare;
    }

    public BookingStatusFilter getBookingStatus() {
        return bookingStatus;
    }

    public Optional<BookingStatusFilter> optionalBookingStatus() {
        return Optional.ofNullable(bookingStatus);
    }

    public BookingStatusFilter bookingStatus() {
        if (bookingStatus == null) {
            setBookingStatus(new BookingStatusFilter());
        }
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatusFilter bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public PaymentModeFilter getModeOfPayment() {
        return modeOfPayment;
    }

    public Optional<PaymentModeFilter> optionalModeOfPayment() {
        return Optional.ofNullable(modeOfPayment);
    }

    public PaymentModeFilter modeOfPayment() {
        if (modeOfPayment == null) {
            setModeOfPayment(new PaymentModeFilter());
        }
        return modeOfPayment;
    }

    public void setModeOfPayment(PaymentModeFilter modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public BooleanFilter getAdditionalServices() {
        return additionalServices;
    }

    public Optional<BooleanFilter> optionalAdditionalServices() {
        return Optional.ofNullable(additionalServices);
    }

    public BooleanFilter additionalServices() {
        if (additionalServices == null) {
            setAdditionalServices(new BooleanFilter());
        }
        return additionalServices;
    }

    public void setAdditionalServices(BooleanFilter additionalServices) {
        this.additionalServices = additionalServices;
    }

    public StringFilter getCoachNumber() {
        return coachNumber;
    }

    public Optional<StringFilter> optionalCoachNumber() {
        return Optional.ofNullable(coachNumber);
    }

    public StringFilter coachNumber() {
        if (coachNumber == null) {
            setCoachNumber(new StringFilter());
        }
        return coachNumber;
    }

    public void setCoachNumber(StringFilter coachNumber) {
        this.coachNumber = coachNumber;
    }

    public StringFilter getSeatNumber() {
        return seatNumber;
    }

    public Optional<StringFilter> optionalSeatNumber() {
        return Optional.ofNullable(seatNumber);
    }

    public StringFilter seatNumber() {
        if (seatNumber == null) {
            setSeatNumber(new StringFilter());
        }
        return seatNumber;
    }

    public void setSeatNumber(StringFilter seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LongFilter getPassengersId() {
        return passengersId;
    }

    public Optional<LongFilter> optionalPassengersId() {
        return Optional.ofNullable(passengersId);
    }

    public LongFilter passengersId() {
        if (passengersId == null) {
            setPassengersId(new LongFilter());
        }
        return passengersId;
    }

    public void setPassengersId(LongFilter passengersId) {
        this.passengersId = passengersId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public Optional<LongFilter> optionalPaymentId() {
        return Optional.ofNullable(paymentId);
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            setPaymentId(new LongFilter());
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getTrainId() {
        return trainId;
    }

    public Optional<LongFilter> optionalTrainId() {
        return Optional.ofNullable(trainId);
    }

    public LongFilter trainId() {
        if (trainId == null) {
            setTrainId(new LongFilter());
        }
        return trainId;
    }

    public void setTrainId(LongFilter trainId) {
        this.trainId = trainId;
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
        final BookingCriteria that = (BookingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pnrNumber, that.pnrNumber) &&
            Objects.equals(bookingDate, that.bookingDate) &&
            Objects.equals(travellingDate, that.travellingDate) &&
            Objects.equals(boardingStation, that.boardingStation) &&
            Objects.equals(destinationStation, that.destinationStation) &&
            Objects.equals(boardingTime, that.boardingTime) &&
            Objects.equals(arrivalTime, that.arrivalTime) &&
            Objects.equals(totalFare, that.totalFare) &&
            Objects.equals(bookingStatus, that.bookingStatus) &&
            Objects.equals(modeOfPayment, that.modeOfPayment) &&
            Objects.equals(additionalServices, that.additionalServices) &&
            Objects.equals(coachNumber, that.coachNumber) &&
            Objects.equals(seatNumber, that.seatNumber) &&
            Objects.equals(passengersId, that.passengersId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(trainId, that.trainId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pnrNumber,
            bookingDate,
            travellingDate,
            boardingStation,
            destinationStation,
            boardingTime,
            arrivalTime,
            totalFare,
            bookingStatus,
            modeOfPayment,
            additionalServices,
            coachNumber,
            seatNumber,
            passengersId,
            paymentId,
            customerId,
            trainId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPnrNumber().map(f -> "pnrNumber=" + f + ", ").orElse("") +
            optionalBookingDate().map(f -> "bookingDate=" + f + ", ").orElse("") +
            optionalTravellingDate().map(f -> "travellingDate=" + f + ", ").orElse("") +
            optionalBoardingStation().map(f -> "boardingStation=" + f + ", ").orElse("") +
            optionalDestinationStation().map(f -> "destinationStation=" + f + ", ").orElse("") +
            optionalBoardingTime().map(f -> "boardingTime=" + f + ", ").orElse("") +
            optionalArrivalTime().map(f -> "arrivalTime=" + f + ", ").orElse("") +
            optionalTotalFare().map(f -> "totalFare=" + f + ", ").orElse("") +
            optionalBookingStatus().map(f -> "bookingStatus=" + f + ", ").orElse("") +
            optionalModeOfPayment().map(f -> "modeOfPayment=" + f + ", ").orElse("") +
            optionalAdditionalServices().map(f -> "additionalServices=" + f + ", ").orElse("") +
            optionalCoachNumber().map(f -> "coachNumber=" + f + ", ").orElse("") +
            optionalSeatNumber().map(f -> "seatNumber=" + f + ", ").orElse("") +
            optionalPassengersId().map(f -> "passengersId=" + f + ", ").orElse("") +
            optionalPaymentId().map(f -> "paymentId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalTrainId().map(f -> "trainId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
