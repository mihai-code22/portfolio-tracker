package com.portfolio.tracker.dto.asset.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BondRequestDTO(
        @NotBlank String symbol,
        @Positive Float quantity,
        @Positive Float buyPrice,
        @Positive Float couponRate,
        @NotNull @Future LocalDate maturityDate
) implements AssetRequestDTO {}
