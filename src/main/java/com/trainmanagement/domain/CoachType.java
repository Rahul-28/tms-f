package com.trainmanagement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoachType.
 */
@Entity
@Table(name = "coach_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoachType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "coach_id", nullable = false, unique = true)
    private String coachId;

    @NotNull
    @Column(name = "coach_name", nullable = false)
    private String coachName;

    @NotNull
    @Column(name = "seat_capacity", nullable = false)
    private Integer seatCapacity;

    @NotNull
    @Column(name = "fare_multiplier", precision = 21, scale = 2, nullable = false)
    private BigDecimal fareMultiplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CoachType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoachId() {
        return this.coachId;
    }

    public CoachType coachId(String coachId) {
        this.setCoachId(coachId);
        return this;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return this.coachName;
    }

    public CoachType coachName(String coachName) {
        this.setCoachName(coachName);
        return this;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getSeatCapacity() {
        return this.seatCapacity;
    }

    public CoachType seatCapacity(Integer seatCapacity) {
        this.setSeatCapacity(seatCapacity);
        return this;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public BigDecimal getFareMultiplier() {
        return this.fareMultiplier;
    }

    public CoachType fareMultiplier(BigDecimal fareMultiplier) {
        this.setFareMultiplier(fareMultiplier);
        return this;
    }

    public void setFareMultiplier(BigDecimal fareMultiplier) {
        this.fareMultiplier = fareMultiplier;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoachType)) {
            return false;
        }
        return getId() != null && getId().equals(((CoachType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoachType{" +
            "id=" + getId() +
            ", coachId='" + getCoachId() + "'" +
            ", coachName='" + getCoachName() + "'" +
            ", seatCapacity=" + getSeatCapacity() +
            ", fareMultiplier=" + getFareMultiplier() +
            "}";
    }
}
