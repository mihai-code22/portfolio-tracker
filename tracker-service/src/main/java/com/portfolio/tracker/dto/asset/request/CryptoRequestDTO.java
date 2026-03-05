package com.portfolio.tracker.dto.asset.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CryptoRequestDTO(
        @NotBlank String symbol,
        @Positive Float quantity,
        @Positive Float buyPrice,
        @NotBlank String blockchain
) implements AssetRequestDTO {}
