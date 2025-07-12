package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trainmanagement.domain.enumeration.BookingStatus;
import com.trainmanagement.domain.enumeration.PaymentMode;
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
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pnr_number", nullable = false, unique = true)
    private String pnrNumber;

    @NotNull
    @Column(name = "booking_date", nullable = false)
    private Instant bookingDate;

    @NotNull
    @Column(name = "travelling_date", nullable = false)
    private LocalDate travellingDate;

    @NotNull
    @Column(name = "boarding_station", nullable = false)
    private String boardingStation;

    @NotNull
    @Column(name = "destination_station", nullable = false)
    private String destinationStation;

    @NotNull
    @Column(name = "boarding_time", nullable = false)
    private Instant boardingTime;

    @NotNull
    @Column(name = "arrival_time", nullable = false)
    private Instant arrivalTime;

    @NotNull
    @Column(name = "total_fare", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalFare;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_of_payment", nullable = false)
    private PaymentMode modeOfPayment;

    @NotNull
    @Column(name = "additional_services", nullable = false)
    private Boolean additionalServices;

    @NotNull
    @Column(name = "coach_number", nullable = false)
    private String coachNumber;

    @NotNull
    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Set<Passenger> passengers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoices", "booking" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bookings" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bookings", "trainCoaches" }, allowSetters = true)
    private Train train;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Booking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPnrNumber() {
        return this.pnrNumber;
    }

    public Booking pnrNumber(String pnrNumber) {
        this.setPnrNumber(pnrNumber);
        return this;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public Instant getBookingDate() {
        return this.bookingDate;
    }

    public Booking bookingDate(Instant bookingDate) {
        this.setBookingDate(bookingDate);
        return this;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getTravellingDate() {
        return this.travellingDate;
    }

    public Booking travellingDate(LocalDate travellingDate) {
        this.setTravellingDate(travellingDate);
        return this;
    }

    public void setTravellingDate(LocalDate travellingDate) {
        this.travellingDate = travellingDate;
    }

    public String getBoardingStation() {
        return this.boardingStation;
    }

    public Booking boardingStation(String boardingStation) {
        this.setBoardingStation(boardingStation);
        return this;
    }

    public void setBoardingStation(String boardingStation) {
        this.boardingStation = boardingStation;
    }

    public String getDestinationStation() {
        return this.destinationStation;
    }

    public Booking destinationStation(String destinationStation) {
        this.setDestinationStation(destinationStation);
        return this;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public Instant getBoardingTime() {
        return this.boardingTime;
    }

    public Booking boardingTime(Instant boardingTime) {
        this.setBoardingTime(boardingTime);
        return this;
    }

    public void setBoardingTime(Instant boardingTime) {
        this.boardingTime = boardingTime;
    }

    public Instant getArrivalTime() {
        return this.arrivalTime;
    }

    public Booking arrivalTime(Instant arrivalTime) {
        this.setArrivalTime(arrivalTime);
        return this;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getTotalFare() {
        return this.totalFare;
    }

    public Booking totalFare(BigDecimal totalFare) {
        this.setTotalFare(totalFare);
        return this;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public BookingStatus getBookingStatus() {
        return this.bookingStatus;
    }

    public Booking bookingStatus(BookingStatus bookingStatus) {
        this.setBookingStatus(bookingStatus);
        return this;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public PaymentMode getModeOfPayment() {
        return this.modeOfPayment;
    }

    public Booking modeOfPayment(PaymentMode modeOfPayment) {
        this.setModeOfPayment(modeOfPayment);
        return this;
    }

    public void setModeOfPayment(PaymentMode modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Boolean getAdditionalServices() {
        return this.additionalServices;
    }

    public Booking additionalServices(Boolean additionalServices) {
        this.setAdditionalServices(additionalServices);
        return this;
    }

    public void setAdditionalServices(Boolean additionalServices) {
        this.additionalServices = additionalServices;
    }

    public String getCoachNumber() {
        return this.coachNumber;
    }

    public Booking coachNumber(String coachNumber) {
        this.setCoachNumber(coachNumber);
        return this;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getSeatNumber() {
        return this.seatNumber;
    }

    public Booking seatNumber(String seatNumber) {
        this.setSeatNumber(seatNumber);
        return this;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Set<Passenger> getPassengers() {
        return this.passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        if (this.passengers != null) {
            this.passengers.forEach(i -> i.setBooking(null));
        }
        if (passengers != null) {
            passengers.forEach(i -> i.setBooking(this));
        }
        this.passengers = passengers;
    }

    public Booking passengers(Set<Passenger> passengers) {
        this.setPassengers(passengers);
        return this;
    }

    public Booking addPassengers(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.setBooking(this);
        return this;
    }

    public Booking removePassengers(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setBooking(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setBooking(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setBooking(this));
        }
        this.payments = payments;
    }

    public Booking payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Booking addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setBooking(this);
        return this;
    }

    public Booking removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setBooking(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Booking customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Train getTrain() {
        return this.train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Booking train(Train train) {
        this.setTrain(train);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return getId() != null && getId().equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
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
            "}";
    }
}
