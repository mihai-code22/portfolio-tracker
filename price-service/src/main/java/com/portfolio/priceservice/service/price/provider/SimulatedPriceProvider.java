package com.portfolio.priceservice.service.price.provider;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component("simulated")
public class SimulatedPriceProvider implements PriceProvider {

    private final Random random = new Random();
    private final Map<String, Float> currentPrices = new ConcurrentHashMap<>();

    @Override
    public Float getPrice(String symbol) {
        return currentPrices.compute(symbol, (key, existingPrice) -> {
            if (existingPrice == null) {
                return 50f + random.nextFloat() * 450f;
            }
            return applyVariation(existingPrice);
        });
    }

    private Float applyVariation(Float price) {
        float variation = (random.nextFloat() - 0.5f) * 0.02f; // ±1%
        return price * (1 + variation);
    }
}
