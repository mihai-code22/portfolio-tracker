package com.portfolio.priceservice.service.price;

import com.portfolio.priceservice.enums.AssetType;

public interface PriceProvider {
    Float getPrice(String symbol, AssetType assetType);
}
