package com.trainmanagement.service.dto;

import com.trainmanagement.domain.enumeration.BookingStatus;
import com.trainmanagement.domain.enumeration.PaymentMode;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.trainmanagement.domain.Booking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingDTO implements Serializable {

    private Long id;

    @NotNull
    private String pnrNumber;

    @NotNull
    private Instant bookingDate;

    @NotNull
    private LocalDate travellingDate;

    @NotNull
    private String boardingStation;

    @NotNull
    private String destinationStation;

    @NotNull
    private Instant boardingTime;

    @NotNull
    private Instant arrivalTime;

    @NotNull
    private BigDecimal totalFare;

    @NotNull
    private BookingStatus bookingStatus;

    @NotNull
    private PaymentMode modeOfPayment;

    @NotNull
    private Boolean additionalServices;

    @NotNull
    private String coachNumber;

    @NotNull
    private String seatNumber;

    private CustomerDTO customer;

    private TrainDTO train;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public Instant getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getTravellingDate() {
        return travellingDate;
    }

    public void setTravellingDate(LocalDate travellingDate) {
        this.travellingDate = travellingDate;
    }

    public String getBoardingStation() {
        return boardingStation;
    }

    public void setBoardingStation(String boardingStation) {
        this.boardingStation = boardingStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public Instant getBoardingTime() {
        return boardingTime;
    }

    public void setBoardingTime(Instant boardingTime) {
        this.boardingTime = boardingTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public PaymentMode getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(PaymentMode modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Boolean getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Boolean additionalServices) {
        this.additionalServices = additionalServices;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public TrainDTO getTrain() {
        return train;
    }

    public void setTrain(TrainDTO train) {
        this.train = train;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", pnrNumber='" + getPnrNumber() + "'" +
            ", bookingDate='" + getBookingDate() + "'" +
            ", travellingDate='" + getTravellingDate() + "'" +
            ", boardingStation='" + getBoardingStation() + "'" +
            ", destinationStation='" + getDestinationStation() + "'" +
            ", boardingTime='" + getBoardingTime() + "'" +
            ", arrivalTime='" + getArrivalTime() + "'" +
            ", totalFare=" + getTotalFare() +
            ", bookingStatus='" + getBookingStatus() + "'" +
            ", modeOfPayment='" + getModeOfPayment() + "'" +
            ", additionalServices='" + getAdditionalServices() + "'" +
            ", coachNumber='" + getCoachNumber() + "'" +
            ", seatNumber='" + getSeatNumber() + "'" +
            ", customer=" + getCustomer() +
            ", train=" + getTrain() +
            "}";
    }
}
