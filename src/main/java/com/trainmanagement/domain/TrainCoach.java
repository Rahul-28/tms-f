package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrainCoach.
 */
@Entity
@Table(name = "train_coach")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainCoach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "train_number", nullable = false)
    private String trainNumber;

    @NotNull
    @Column(name = "seat_capacity", nullable = false)
    private Integer seatCapacity;

    @NotNull
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @NotNull
    @Column(name = "fare_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal farePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private CoachType coachType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bookings", "trainCoaches" }, allowSetters = true)
    private Train train;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrainCoach id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return this.trainNumber;
    }

    public TrainCoach trainNumber(String trainNumber) {
        this.setTrainNumber(trainNumber);
        return this;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Integer getSeatCapacity() {
        return this.seatCapacity;
    }

    public TrainCoach seatCapacity(Integer seatCapacity) {
        this.setSeatCapacity(seatCapacity);
        return this;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public Integer getAvailableSeats() {
        return this.availableSeats;
    }

    public TrainCoach availableSeats(Integer availableSeats) {
        this.setAvailableSeats(availableSeats);
        return this;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getFarePrice() {
        return this.farePrice;
    }

    public TrainCoach farePrice(BigDecimal farePrice) {
        this.setFarePrice(farePrice);
        return this;
    }

    public void setFarePrice(BigDecimal farePrice) {
        this.farePrice = farePrice;
    }

    public CoachType getCoachType() {
        return this.coachType;
    }

    public void setCoachType(CoachType coachType) {
        this.coachType = coachType;
    }

    public TrainCoach coachType(CoachType coachType) {
        this.setCoachType(coachType);
        return this;
    }

    public Train getTrain() {
        return this.train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public TrainCoach train(Train train) {
        this.setTrain(train);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainCoach)) {
            return false;
        }
        return getId() != null && getId().equals(((TrainCoach) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainCoach{" +
            "id=" + getId() +
            ", trainNumber='" + getTrainNumber() + "'" +
            ", seatCapacity=" + getSeatCapacity() +
            ", availableSeats=" + getAvailableSeats() +
            ", farePrice=" + getFarePrice() +
            "}";
    }
}
