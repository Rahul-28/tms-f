package com.trainmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trainmanagement.domain.enumeration.TransactionStatus;
import com.trainmanagement.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @NotNull
    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private Instant invoiceDate;

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

    @NotNull
    @Column(name = "customer_details", nullable = false)
    private String customerDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "invoices", "booking" }, allowSetters = true)
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Invoice transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptNumber() {
        return this.receiptNumber;
    }

    public Invoice receiptNumber(String receiptNumber) {
        this.setReceiptNumber(receiptNumber);
        return this;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Instant getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(Instant invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public Invoice transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return this.transactionAmount;
    }

    public Invoice transactionAmount(BigDecimal transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public Invoice transactionStatus(TransactionStatus transactionStatus) {
        this.setTransactionStatus(transactionStatus);
        return this;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getCustomerDetails() {
        return this.customerDetails;
    }

    public Invoice customerDetails(String customerDetails) {
        this.setCustomerDetails(customerDetails);
        return this;
    }

    public void setCustomerDetails(String customerDetails) {
        this.customerDetails = customerDetails;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Invoice payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            ", transactionStatus='" + getTransactionStatus() + "'" +
            ", customerDetails='" + getCustomerDetails() + "'" +
            "}";
    }
}
