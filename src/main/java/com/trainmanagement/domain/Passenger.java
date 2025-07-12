package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @NotNull
    @Min(value = 1)
    @Max(value = 120)
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Column(name = "coach_number", nullable = false)
    private String coachNumber;

    @NotNull
    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "passengers", "payments", "customer", "train" }, allowSetters = true)
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Passenger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassengerName() {
        return this.passengerName;
    }

    public Passenger passengerName(String passengerName) {
        this.setPassengerName(passengerName);
        return this;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getAge() {
        return this.age;
    }

    public Passenger age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCoachNumber() {
        return this.coachNumber;
    }

    public Passenger coachNumber(String coachNumber) {
        this.setCoachNumber(coachNumber);
        return this;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getSeatNumber() {
        return this.seatNumber;
    }

    public Passenger seatNumber(String seatNumber) {
        this.setSeatNumber(seatNumber);
        return this;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Passenger booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Passenger)) {
            return false;
        }
        return getId() != null && getId().equals(((Passenger) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Passenger{" +
            "id=" + getId() +
            ", passengerName='" + getPassengerName() + "'" +
            ", age=" + getAge() +
            ", coachNumber='" + getCoachNumber() + "'" +
            ", seatNumber='" + getSeatNumber() + "'" +
            "}";
    }
}
