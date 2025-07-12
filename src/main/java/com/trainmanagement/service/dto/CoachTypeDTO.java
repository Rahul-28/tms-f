package com.trainmanagement.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.trainmanagement.domain.CoachType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoachTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String coachId;

    @NotNull
    private String coachName;

    @NotNull
    private Integer seatCapacity;

    @NotNull
    private BigDecimal fareMultiplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public BigDecimal getFareMultiplier() {
        return fareMultiplier;
    }

    public void setFareMultiplier(BigDecimal fareMultiplier) {
        this.fareMultiplier = fareMultiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoachTypeDTO)) {
            return false;
        }

        CoachTypeDTO coachTypeDTO = (CoachTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coachTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoachTypeDTO{" +
            "id=" + getId() +
            ", coachId='" + getCoachId() + "'" +
            ", coachName='" + getCoachName() + "'" +
            ", seatCapacity=" + getSeatCapacity() +
            ", fareMultiplier=" + getFareMultiplier() +
            "}";
    }
}
