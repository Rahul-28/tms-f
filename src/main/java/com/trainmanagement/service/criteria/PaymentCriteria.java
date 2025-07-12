package com.trainmanagement.service.criteria;

import com.trainmanagement.domain.enumeration.TransactionStatus;
import com.trainmanagement.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.trainmanagement.domain.Payment} entity. This class is used
 * in {@link com.trainmanagement.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {}

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }
    }

    /**
     * Class for filtering TransactionStatus
     */
    public static class TransactionStatusFilter extends Filter<TransactionStatus> {

        public TransactionStatusFilter() {}

        public TransactionStatusFilter(TransactionStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransactionStatusFilter copy() {
            return new TransactionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentId;

    private StringFilter transactionId;

    private StringFilter receiptNumber;

    private InstantFilter transactionDate;

    private TransactionTypeFilter transactionType;

    private BigDecimalFilter transactionAmount;

    private TransactionStatusFilter transactionStatus;

    private StringFilter cardNumber;

    private StringFilter expiryDate;

    private StringFilter cvv;

    private StringFilter cardholderName;

    private LongFilter invoicesId;

    private LongFilter bookingId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.paymentId = other.optionalPaymentId().map(StringFilter::copy).orElse(null);
        this.transactionId = other.optionalTransactionId().map(StringFilter::copy).orElse(null);
        this.receiptNumber = other.optionalReceiptNumber().map(StringFilter::copy).orElse(null);
        this.transactionDate = other.optionalTransactionDate().map(InstantFilter::copy).orElse(null);
        this.transactionType = other.optionalTransactionType().map(TransactionTypeFilter::copy).orElse(null);
        this.transactionAmount = other.optionalTransactionAmount().map(BigDecimalFilter::copy).orElse(null);
        this.transactionStatus = other.optionalTransactionStatus().map(TransactionStatusFilter::copy).orElse(null);
        this.cardNumber = other.optionalCardNumber().map(StringFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(StringFilter::copy).orElse(null);
        this.cvv = other.optionalCvv().map(StringFilter::copy).orElse(null);
        this.cardholderName = other.optionalCardholderName().map(StringFilter::copy).orElse(null);
        this.invoicesId = other.optionalInvoicesId().map(LongFilter::copy).orElse(null);
        this.bookingId = other.optionalBookingId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public StringFilter getPaymentId() {
        return paymentId;
    }

    public Optional<StringFilter> optionalPaymentId() {
        return Optional.ofNullable(paymentId);
    }

    public StringFilter paymentId() {
        if (paymentId == null) {
            setPaymentId(new StringFilter());
        }
        return paymentId;
    }

    public void setPaymentId(StringFilter paymentId) {
        this.paymentId = paymentId;
    }

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public Optional<StringFilter> optionalTransactionId() {
        return Optional.ofNullable(transactionId);
    }

    public StringFilter transactionId() {
        if (transactionId == null) {
            setTransactionId(new StringFilter());
        }
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public StringFilter getReceiptNumber() {
        return receiptNumber;
    }

    public Optional<StringFilter> optionalReceiptNumber() {
        return Optional.ofNullable(receiptNumber);
    }

    public StringFilter receiptNumber() {
        if (receiptNumber == null) {
            setReceiptNumber(new StringFilter());
        }
        return receiptNumber;
    }

    public void setReceiptNumber(StringFilter receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public InstantFilter getTransactionDate() {
        return transactionDate;
    }

    public Optional<InstantFilter> optionalTransactionDate() {
        return Optional.ofNullable(transactionDate);
    }

    public InstantFilter transactionDate() {
        if (transactionDate == null) {
            setTransactionDate(new InstantFilter());
        }
        return transactionDate;
    }

    public void setTransactionDate(InstantFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public Optional<TransactionTypeFilter> optionalTransactionType() {
        return Optional.ofNullable(transactionType);
    }

    public TransactionTypeFilter transactionType() {
        if (transactionType == null) {
            setTransactionType(new TransactionTypeFilter());
        }
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimalFilter getTransactionAmount() {
        return transactionAmount;
    }

    public Optional<BigDecimalFilter> optionalTransactionAmount() {
        return Optional.ofNullable(transactionAmount);
    }

    public BigDecimalFilter transactionAmount() {
        if (transactionAmount == null) {
            setTransactionAmount(new BigDecimalFilter());
        }
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimalFilter transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionStatusFilter getTransactionStatus() {
        return transactionStatus;
    }

    public Optional<TransactionStatusFilter> optionalTransactionStatus() {
        return Optional.ofNullable(transactionStatus);
    }

    public TransactionStatusFilter transactionStatus() {
        if (transactionStatus == null) {
            setTransactionStatus(new TransactionStatusFilter());
        }
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatusFilter transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public StringFilter getCardNumber() {
        return cardNumber;
    }

    public Optional<StringFilter> optionalCardNumber() {
        return Optional.ofNullable(cardNumber);
    }

    public StringFilter cardNumber() {
        if (cardNumber == null) {
            setCardNumber(new StringFilter());
        }
        return cardNumber;
    }

    public void setCardNumber(StringFilter cardNumber) {
        this.cardNumber = cardNumber;
    }

    public StringFilter getExpiryDate() {
        return expiryDate;
    }

    public Optional<StringFilter> optionalExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    public StringFilter expiryDate() {
        if (expiryDate == null) {
            setExpiryDate(new StringFilter());
        }
        return expiryDate;
    }

    public void setExpiryDate(StringFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public StringFilter getCvv() {
        return cvv;
    }

    public Optional<StringFilter> optionalCvv() {
        return Optional.ofNullable(cvv);
    }

    public StringFilter cvv() {
        if (cvv == null) {
            setCvv(new StringFilter());
        }
        return cvv;
    }

    public void setCvv(StringFilter cvv) {
        this.cvv = cvv;
    }

    public StringFilter getCardholderName() {
        return cardholderName;
    }

    public Optional<StringFilter> optionalCardholderName() {
        return Optional.ofNullable(cardholderName);
    }

    public StringFilter cardholderName() {
        if (cardholderName == null) {
            setCardholderName(new StringFilter());
        }
        return cardholderName;
    }

    public void setCardholderName(StringFilter cardholderName) {
        this.cardholderName = cardholderName;
    }

    public LongFilter getInvoicesId() {
        return invoicesId;
    }

    public Optional<LongFilter> optionalInvoicesId() {
        return Optional.ofNullable(invoicesId);
    }

    public LongFilter invoicesId() {
        if (invoicesId == null) {
            setInvoicesId(new LongFilter());
        }
        return invoicesId;
    }

    public void setInvoicesId(LongFilter invoicesId) {
        this.invoicesId = invoicesId;
    }

    public LongFilter getBookingId() {
        return bookingId;
    }

    public Optional<LongFilter> optionalBookingId() {
        return Optional.ofNullable(bookingId);
    }

    public LongFilter bookingId() {
        if (bookingId == null) {
            setBookingId(new LongFilter());
        }
        return bookingId;
    }

    public void setBookingId(LongFilter bookingId) {
        this.bookingId = bookingId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(receiptNumber, that.receiptNumber) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(transactionAmount, that.transactionAmount) &&
            Objects.equals(transactionStatus, that.transactionStatus) &&
            Objects.equals(cardNumber, that.cardNumber) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(cvv, that.cvv) &&
            Objects.equals(cardholderName, that.cardholderName) &&
            Objects.equals(invoicesId, that.invoicesId) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            paymentId,
            transactionId,
            receiptNumber,
            transactionDate,
            transactionType,
            transactionAmount,
            transactionStatus,
            cardNumber,
            expiryDate,
            cvv,
            cardholderName,
            invoicesId,
            bookingId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPaymentId().map(f -> "paymentId=" + f + ", ").orElse("") +
            optionalTransactionId().map(f -> "transactionId=" + f + ", ").orElse("") +
            optionalReceiptNumber().map(f -> "receiptNumber=" + f + ", ").orElse("") +
            optionalTransactionDate().map(f -> "transactionDate=" + f + ", ").orElse("") +
            optionalTransactionType().map(f -> "transactionType=" + f + ", ").orElse("") +
            optionalTransactionAmount().map(f -> "transactionAmount=" + f + ", ").orElse("") +
            optionalTransactionStatus().map(f -> "transactionStatus=" + f + ", ").orElse("") +
            optionalCardNumber().map(f -> "cardNumber=" + f + ", ").orElse("") +
            optionalExpiryDate().map(f -> "expiryDate=" + f + ", ").orElse("") +
            optionalCvv().map(f -> "cvv=" + f + ", ").orElse("") +
            optionalCardholderName().map(f -> "cardholderName=" + f + ", ").orElse("") +
            optionalInvoicesId().map(f -> "invoicesId=" + f + ", ").orElse("") +
            optionalBookingId().map(f -> "bookingId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
