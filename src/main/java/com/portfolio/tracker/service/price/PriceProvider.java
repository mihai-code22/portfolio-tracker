package com.portfolio.tracker.service.price;

import com.portfolio.tracker.enums.AssetType;

public interface PriceProvider {
    Float getPrice(String symbol, AssetType assetType);
}
