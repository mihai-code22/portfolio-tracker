package com.portfolio.tracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetType {

    STOCK("STOCK"),
    BOND("BOND"),
    CRYPTO("CRYPTO");

    private final String type;
}
