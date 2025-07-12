package com.trainmanagement.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.trainmanagement.domain.Passenger} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PassengerDTO implements Serializable {

    private Long id;

    @NotNull
    private String passengerName;

    @NotNull
    @Min(value = 1)
    @Max(value = 120)
    private Integer age;

    @NotNull
    private String coachNumber;

    @NotNull
    private String seatNumber;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassengerDTO)) {
            return false;
        }

        PassengerDTO passengerDTO = (PassengerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, passengerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassengerDTO{" +
            "id=" + getId() +
            ", passengerName='" + getPassengerName() + "'" +
            ", age=" + getAge() +
            ", coachNumber='" + getCoachNumber() + "'" +
            ", seatNumber='" + getSeatNumber() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}
