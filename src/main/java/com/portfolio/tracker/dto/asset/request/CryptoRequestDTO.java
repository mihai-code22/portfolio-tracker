package com.portfolio.tracker.dto.asset.request;

public record CryptoRequestDTO(
        String symbol,
        Float quantity,
        Float buyPrice,
        String blockchain
) implements AssetRequestDTO {}
