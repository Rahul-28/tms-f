package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trainmanagement.domain.enumeration.TransactionStatus;
import com.trainmanagement.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "payment_id", nullable = false, unique = true)
    private String paymentId;

    @NotNull
    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @NotNull
    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @NotNull
    @Column(name = "transaction_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Pattern(regexp = "^[0-9]{16}$")
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Pattern(regexp = "^[0-9]{3}$")
    @Column(name = "cvv")
    private String cvv;

    @Column(name = "cardholder_name")
    private String cardholderName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "passengers", "payments", "customer", "train" }, allowSetters = true)
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    public Payment paymentId(String paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Payment transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptNumber() {
        return this.receiptNumber;
    }

    public Payment receiptNumber(String receiptNumber) {
        this.setReceiptNumber(receiptNumber);
        return this;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public Payment transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public Payment transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return this.transactionAmount;
    }

    public Payment transactionAmount(BigDecimal transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public Payment transactionStatus(TransactionStatus transactionStatus) {
        this.setTransactionStatus(transactionStatus);
        return this;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public Payment cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public Payment expiryDate(String expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return this.cvv;
    }

    public Payment cvv(String cvv) {
        this.setCvv(cvv);
        return this;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardholderName() {
        return this.cardholderName;
    }

    public Payment cardholderName(String cardholderName) {
        this.setCardholderName(cardholderName);
        return this;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setPayment(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setPayment(this));
        }
        this.invoices = invoices;
    }

    public Payment invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Payment addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setPayment(this);
        return this;
    }

    public Payment removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setPayment(null);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Payment booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
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
            "}";
    }
}
