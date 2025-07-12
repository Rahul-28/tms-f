package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "customer_id", nullable = false, unique = true)
    private String customerId;

    @NotNull
    @Size(min = 3)
    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Size(min = 8)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]{12}$")
    @Column(name = "aadhaar_number", nullable = false)
    private String aadhaarNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_information")
    private String contactInformation;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "passengers", "payments", "customer", "train" }, allowSetters = true)
    private Set<Booking> bookings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Customer customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return this.username;
    }

    public Customer username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Customer password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public Customer mobileNumber(String mobileNumber) {
        this.setMobileNumber(mobileNumber);
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAadhaarNumber() {
        return this.aadhaarNumber;
    }

    public Customer aadhaarNumber(String aadhaarNumber) {
        this.setAadhaarNumber(aadhaarNumber);
        return this;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInformation() {
        return this.contactInformation;
    }

    public Customer contactInformation(String contactInformation) {
        this.setContactInformation(contactInformation);
        return this;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public Instant getRegistrationDate() {
        return this.registrationDate;
    }

    public Customer registrationDate(Instant registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Customer isActive(Boolean isActive) {
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
            this.bookings.forEach(i -> i.setCustomer(null));
        }
        if (bookings != null) {
            bookings.forEach(i -> i.setCustomer(this));
        }
        this.bookings = bookings;
    }

    public Customer bookings(Set<Booking> bookings) {
        this.setBookings(bookings);
        return this;
    }

    public Customer addBookings(Booking booking) {
        this.bookings.add(booking);
        booking.setCustomer(this);
        return this;
    }

    public Customer removeBookings(Booking booking) {
        this.bookings.remove(booking);
        booking.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", aadhaarNumber='" + getAadhaarNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", contactInformation='" + getContactInformation() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
