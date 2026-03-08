package com.portfolio.tracker.service.price;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PriceCache {

    private final Cache<String, Float> cache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(1000)
            .recordStats()
            .build();

    public void update(String symbol, Float price) {
        cache.put(symbol, price);
    }

    public Float getCurrentPrice(String symbol) {
        return cache.getIfPresent(symbol);
    }

    public CacheStats getStats() {
        return cache.stats();
    }
}
