package com.portfolio.tracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetType {

    STOCK("Stock"),
    BOND("Bond"),
    CRYPTO("Crypto");

    private final String type;
}
