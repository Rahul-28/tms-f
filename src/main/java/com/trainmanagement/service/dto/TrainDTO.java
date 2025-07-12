package com.trainmanagement.service.dto;

import com.trainmanagement.domain.enumeration.ServiceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.trainmanagement.domain.Train} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainDTO implements Serializable {

    private Long id;

    @NotNull
    private String trainNumber;

    @NotNull
    private String trainName;

    @NotNull
    private String origin;

    @NotNull
    private String destination;

    @NotNull
    private String intermediateStop;

    @NotNull
    private LocalDate serviceStartDate;

    @NotNull
    private LocalDate serviceEndDate;

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private Instant departureTime;

    @NotNull
    private Instant arrivalTime;

    @NotNull
    private BigDecimal basicPrice;

    @NotNull
    private Boolean isActive;

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

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getIntermediateStop() {
        return intermediateStop;
    }

    public void setIntermediateStop(String intermediateStop) {
        this.intermediateStop = intermediateStop;
    }

    public LocalDate getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDate serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDate getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDate serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainDTO)) {
            return false;
        }

        TrainDTO trainDTO = (TrainDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainDTO{" +
            "id=" + getId() +
            ", trainNumber='" + getTrainNumber() + "'" +
            ", trainName='" + getTrainName() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", destination='" + getDestination() + "'" +
            ", intermediateStop='" + getIntermediateStop() + "'" +
            ", serviceStartDate='" + getServiceStartDate() + "'" +
            ", serviceEndDate='" + getServiceEndDate() + "'" +
            ", serviceType='" + getServiceType() + "'" +
            ", departureTime='" + getDepartureTime() + "'" +
            ", arrivalTime='" + getArrivalTime() + "'" +
            ", basicPrice=" + getBasicPrice() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
