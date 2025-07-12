package com.trainmanagement.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trainmanagement.domain.Customer} entity. This class is used
 * in {@link com.trainmanagement.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerId;

    private StringFilter username;

    private StringFilter password;

    private StringFilter email;

    private StringFilter mobileNumber;

    private StringFilter aadhaarNumber;

    private StringFilter address;

    private StringFilter contactInformation;

    private InstantFilter registrationDate;

    private BooleanFilter isActive;

    private LongFilter bookingsId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(StringFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.password = other.optionalPassword().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.mobileNumber = other.optionalMobileNumber().map(StringFilter::copy).orElse(null);
        this.aadhaarNumber = other.optionalAadhaarNumber().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.contactInformation = other.optionalContactInformation().map(StringFilter::copy).orElse(null);
        this.registrationDate = other.optionalRegistrationDate().map(InstantFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.bookingsId = other.optionalBookingsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getCustomerId() {
        return customerId;
    }

    public Optional<StringFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public StringFilter customerId() {
        if (customerId == null) {
            setCustomerId(new StringFilter());
        }
        return customerId;
    }

    public void setCustomerId(StringFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPassword() {
        return password;
    }

    public Optional<StringFilter> optionalPassword() {
        return Optional.ofNullable(password);
    }

    public StringFilter password() {
        if (password == null) {
            setPassword(new StringFilter());
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getMobileNumber() {
        return mobileNumber;
    }

    public Optional<StringFilter> optionalMobileNumber() {
        return Optional.ofNullable(mobileNumber);
    }

    public StringFilter mobileNumber() {
        if (mobileNumber == null) {
            setMobileNumber(new StringFilter());
        }
        return mobileNumber;
    }

    public void setMobileNumber(StringFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public StringFilter getAadhaarNumber() {
        return aadhaarNumber;
    }

    public Optional<StringFilter> optionalAadhaarNumber() {
        return Optional.ofNullable(aadhaarNumber);
    }

    public StringFilter aadhaarNumber() {
        if (aadhaarNumber == null) {
            setAadhaarNumber(new StringFilter());
        }
        return aadhaarNumber;
    }

    public void setAadhaarNumber(StringFilter aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getContactInformation() {
        return contactInformation;
    }

    public Optional<StringFilter> optionalContactInformation() {
        return Optional.ofNullable(contactInformation);
    }

    public StringFilter contactInformation() {
        if (contactInformation == null) {
            setContactInformation(new StringFilter());
        }
        return contactInformation;
    }

    public void setContactInformation(StringFilter contactInformation) {
        this.contactInformation = contactInformation;
    }

    public InstantFilter getRegistrationDate() {
        return registrationDate;
    }

    public Optional<InstantFilter> optionalRegistrationDate() {
        return Optional.ofNullable(registrationDate);
    }

    public InstantFilter registrationDate() {
        if (registrationDate == null) {
            setRegistrationDate(new InstantFilter());
        }
        return registrationDate;
    }

    public void setRegistrationDate(InstantFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getBookingsId() {
        return bookingsId;
    }

    public Optional<LongFilter> optionalBookingsId() {
        return Optional.ofNullable(bookingsId);
    }

    public LongFilter bookingsId() {
        if (bookingsId == null) {
            setBookingsId(new LongFilter());
        }
        return bookingsId;
    }

    public void setBookingsId(LongFilter bookingsId) {
        this.bookingsId = bookingsId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(email, that.email) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(aadhaarNumber, that.aadhaarNumber) &&
            Objects.equals(address, that.address) &&
            Objects.equals(contactInformation, that.contactInformation) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(bookingsId, that.bookingsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            customerId,
            username,
            password,
            email,
            mobileNumber,
            aadhaarNumber,
            address,
            contactInformation,
            registrationDate,
            isActive,
            bookingsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalPassword().map(f -> "password=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalMobileNumber().map(f -> "mobileNumber=" + f + ", ").orElse("") +
            optionalAadhaarNumber().map(f -> "aadhaarNumber=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalContactInformation().map(f -> "contactInformation=" + f + ", ").orElse("") +
            optionalRegistrationDate().map(f -> "registrationDate=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalBookingsId().map(f -> "bookingsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
