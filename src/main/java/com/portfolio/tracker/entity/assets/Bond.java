package com.portfolio.tracker.entity.assets;

import com.portfolio.tracker.entity.Asset;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("BOND")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bond extends Asset {

    @NotNull
    @Positive
    @Column(name = "coupon_rate", updatable = false)
    private Float couponRate;

    @Column(name = "maturity_date", nullable = false, updatable = false)
    private LocalDate  maturityDate;
}
