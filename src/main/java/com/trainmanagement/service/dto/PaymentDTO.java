package com.trainmanagement.service.dto;

import com.trainmanagement.domain.enumeration.TransactionStatus;
import com.trainmanagement.domain.enumeration.TransactionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.trainmanagement.domain.Payment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private String paymentId;

    @NotNull
    private String transactionId;

    @NotNull
    private String receiptNumber;

    @NotNull
    private Instant transactionDate;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private BigDecimal transactionAmount;

    @NotNull
    private TransactionStatus transactionStatus;

    @Pattern(regexp = "^[0-9]{16}$")
    private String cardNumber;

    private String expiryDate;

    @Pattern(regexp = "^[0-9]{3}$")
    private String cvv;

    private String cardholderName;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
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
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", paymentId='" + getPaymentId() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            ", transactionStatus='" + getTransactionStatus() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", cvv='" + getCvv() + "'" +
            ", cardholderName='" + getCardholderName() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}
