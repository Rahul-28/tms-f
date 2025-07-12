package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trainmanagement.domain.enumeration.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Train.
 */
@Entity
@Table(name = "train")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "train_number", nullable = false, unique = true)
    private String trainNumber;

    @NotNull
    @Column(name = "train_name", nullable = false, unique = true)
    private String trainName;

    @NotNull
    @Column(name = "origin", nullable = false)
    private String origin;

    @NotNull
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull
    @Column(name = "intermediate_stop", nullable = false)
    private String intermediateStop;

    @NotNull
    @Column(name = "service_start_date", nullable = false)
    private LocalDate serviceStartDate;

    @NotNull
    @Column(name = "service_end_date", nullable = false)
    private LocalDate serviceEndDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @NotNull
    @Column(name = "departure_time", nullable = false)
    private Instant departureTime;

    @NotNull
    @Column(name = "arrival_time", nullable = false)
    private Instant arrivalTime;

    @NotNull
    @Column(name = "basic_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal basicPrice;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "train")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "passengers", "payments", "customer", "train" }, allowSetters = true)
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "train")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coachType", "train" }, allowSetters = true)
    private Set<TrainCoach> trainCoaches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Train id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return this.trainNumber;
    }

    public Train trainNumber(String trainNumber) {
        this.setTrainNumber(trainNumber);
        return this;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return this.trainName;
    }

    public Train trainName(String trainName) {
        this.setTrainName(trainName);
        return this;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Train origin(String origin) {
        this.setOrigin(origin);
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public Train destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getIntermediateStop() {
        return this.intermediateStop;
    }

    public Train intermediateStop(String intermediateStop) {
        this.setIntermediateStop(intermediateStop);
        return this;
    }

    public void setIntermediateStop(String intermediateStop) {
        this.intermediateStop = intermediateStop;
    }

    public LocalDate getServiceStartDate() {
        return this.serviceStartDate;
    }

    public Train serviceStartDate(LocalDate serviceStartDate) {
        this.setServiceStartDate(serviceStartDate);
        return this;
    }

    public void setServiceStartDate(LocalDate serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDate getServiceEndDate() {
        return this.serviceEndDate;
    }

    public Train serviceEndDate(LocalDate serviceEndDate) {
        this.setServiceEndDate(serviceEndDate);
        return this;
    }

    public void setServiceEndDate(LocalDate serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public ServiceType getServiceType() {
        return this.serviceType;
    }

    public Train serviceType(ServiceType serviceType) {
        this.setServiceType(serviceType);
        return this;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Instant getDepartureTime() {
        return this.departureTime;
    }

    public Train departureTime(Instant departureTime) {
        this.setDepartureTime(departureTime);
        return this;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return this.arrivalTime;
    }

    public Train arrivalTime(Instant arrivalTime) {
        this.setArrivalTime(arrivalTime);
        return this;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getBasicPrice() {
        return this.basicPrice;
    }

    public Train basicPrice(BigDecimal basicPrice) {
        this.setBasicPrice(basicPrice);
        return this;
    }

    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Train isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Booking> getBookings() {
        return this.bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        if (this.bookings != null) {
            this.bookings.forEach(i -> i.setTrain(null));
        }
        if (bookings != null) {
            bookings.forEach(i -> i.setTrain(this));
        }
        this.bookings = bookings;
    }

    public Train bookings(Set<Booking> bookings) {
        this.setBookings(bookings);
        return this;
    }

    public Train addBookings(Booking booking) {
        this.bookings.add(booking);
        booking.setTrain(this);
        return this;
    }

    public Train removeBookings(Booking booking) {
        this.bookings.remove(booking);
        booking.setTrain(null);
        return this;
    }

    public Set<TrainCoach> getTrainCoaches() {
        return this.trainCoaches;
    }

    public void setTrainCoaches(Set<TrainCoach> trainCoaches) {
        if (this.trainCoaches != null) {
            this.trainCoaches.forEach(i -> i.setTrain(null));
        }
        if (trainCoaches != null) {
            trainCoaches.forEach(i -> i.setTrain(this));
        }
        this.trainCoaches = trainCoaches;
    }

    public Train trainCoaches(Set<TrainCoach> trainCoaches) {
        this.setTrainCoaches(trainCoaches);
        return this;
    }

    public Train addTrainCoaches(TrainCoach trainCoach) {
        this.trainCoaches.add(trainCoach);
        trainCoach.setTrain(this);
        return this;
    }

    public Train removeTrainCoaches(TrainCoach trainCoach) {
        this.trainCoaches.remove(trainCoach);
        trainCoach.setTrain(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Train)) {
            return false;
        }
        return getId() != null && getId().equals(((Train) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Train{" +
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
