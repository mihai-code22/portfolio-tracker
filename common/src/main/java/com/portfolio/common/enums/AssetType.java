package com.portfolio.common.enums;

public enum AssetType {

    STOCK("STOCK"),
    BOND("BOND"),
    CRYPTO("CRYPTO");

    private final String type;

    AssetType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
