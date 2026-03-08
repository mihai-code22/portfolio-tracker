package com.portfolio.priceservice.service.price;

import com.portfolio.common.enums.AssetType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component("simulated")
public class SimulatedPriceProvider implements PriceProvider {

    private final Random random = new Random();
    private final Map<String, Float> currentPrices = new ConcurrentHashMap<>();

    @Override
    public Float getPrice(String symbol, AssetType assetType) {
        return currentPrices.compute(symbol, (key, existingPrice) -> {
            if (existingPrice == null) {
                return generateInitialPrice(assetType);
            }
            return applyVariation(existingPrice);
        });
    }

    private Float generateInitialPrice(AssetType assetType) {
        return switch (assetType) {
            case STOCK -> 50f + random.nextFloat() * 450f;   // $50 - $500
            case CRYPTO -> 100f + random.nextFloat() * 9900f; // $100 - $10000
            case BOND -> 95f + random.nextFloat() * 10f;      // $95 - $105
        };
    }

    private Float applyVariation(Float price) {
        float variation = (random.nextFloat() - 0.5f) * 0.02f; // ±1%
        return price * (1 + variation);
    }
}
