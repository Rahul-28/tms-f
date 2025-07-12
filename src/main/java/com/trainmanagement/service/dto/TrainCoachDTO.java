package com.trainmanagement.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.trainmanagement.domain.TrainCoach} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainCoachDTO implements Serializable {

    private Long id;

    @NotNull
    private String trainNumber;

    @NotNull
    private Integer seatCapacity;

    @NotNull
    private Integer availableSeats;

    @NotNull
    private BigDecimal farePrice;

    private CoachTypeDTO coachType;

    private TrainDTO train;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Integer getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getFarePrice() {
        return farePrice;
    }

    public void setFarePrice(BigDecimal farePrice) {
        this.farePrice = farePrice;
    }

    public CoachTypeDTO getCoachType() {
        return coachType;
    }

    public void setCoachType(CoachTypeDTO coachType) {
        this.coachType = coachType;
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
        if (!(o instanceof TrainCoachDTO)) {
            return false;
        }

        TrainCoachDTO trainCoachDTO = (TrainCoachDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainCoachDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainCoachDTO{" +
            "id=" + getId() +
            ", trainNumber='" + getTrainNumber() + "'" +
            ", seatCapacity=" + getSeatCapacity() +
            ", availableSeats=" + getAvailableSeats() +
            ", farePrice=" + getFarePrice() +
            ", coachType=" + getCoachType() +
            ", train=" + getTrain() +
            "}";
    }
}
