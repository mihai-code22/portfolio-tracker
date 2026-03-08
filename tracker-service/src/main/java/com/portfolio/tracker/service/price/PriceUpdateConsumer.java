package com.portfolio.tracker.service.price;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.portfolio.common.event.PriceUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PriceUpdateConsumer implements PriceService {

    private final Cache<String, Float> currentPrices = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(1000)
            .recordStats()
            .build();

    @KafkaListener(
            topics = "${kafka.topics.price-updates.name}",
            groupId = "${kafka.consumer.group-id}"
    )
    public void consume(PriceUpdateEvent event) {
        log.debug("Received price update for {}: {}", event.symbol(), event.price());
        currentPrices.put(event.symbol(), event.price());
    }

    @Override
    public Float getCurrentPrice(String symbol) {
        return currentPrices.getIfPresent(symbol);
    }

    public CacheStats getStats() {
        return currentPrices.stats();
    }
}
