package com.portfolio.tracker.entity.assets;

import com.portfolio.tracker.entity.Asset;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("BOND")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bond extends Asset {

    @NotNull
    @Positive
    @Column(name = "coupon_rate", updatable = false)
    private Float couponRate;

    @Column(name = "maturity_date", updatable = false)
    private LocalDate  maturityDate;
}
