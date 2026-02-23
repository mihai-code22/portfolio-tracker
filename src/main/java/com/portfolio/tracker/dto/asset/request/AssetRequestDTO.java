package com.portfolio.tracker.dto.asset.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StockRequestDTO.class, name = "STOCK"),
        @JsonSubTypes.Type(value = BondRequestDTO.class, name = "BOND"),
        @JsonSubTypes.Type(value = CryptoRequestDTO.class, name = "CRYPTO")
})
public sealed interface AssetRequestDTO
    permits StockRequestDTO, BondRequestDTO, CryptoRequestDTO {

    String symbol();
    Float quantity();
    Float buyPrice();
}
