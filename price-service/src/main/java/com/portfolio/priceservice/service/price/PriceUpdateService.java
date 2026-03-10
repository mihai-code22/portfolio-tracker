package com.portfolio.priceservice.service.price;

import com.portfolio.priceservice.entity.mongo.PriceHistory;
import com.portfolio.priceservice.entity.postgres.Asset;
import com.portfolio.common.enums.AssetType;
import com.portfolio.common.event.PriceUpdateEvent;
import com.portfolio.priceservice.entity.postgres.assets.Crypto;
import com.portfolio.priceservice.entity.postgres.assets.Stock;
import com.portfolio.priceservice.repository.mongo.PriceHistoryRepository;
import com.portfolio.priceservice.repository.postgres.AssetRepository;
import com.portfolio.priceservice.service.kafka.PriceEventPublisher;
import com.portfolio.priceservice.service.price.provider.PriceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceUpdateService {

    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceProvider simulatedPriceProvider;
    private final PriceProvider binancePriceProvider;
    private final PriceEventPublisher priceEventPublisher;

    public PriceUpdateService(AssetRepository assetRepository,
                              PriceHistoryRepository priceHistoryRepository,
                              @Qualifier("simulated") PriceProvider simulatedPriceProvider,
                              @Qualifier("binance") PriceProvider binancePriceProvider,
                              PriceEventPublisher priceEventPublisher) {
        this.assetRepository = assetRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.simulatedPriceProvider = simulatedPriceProvider;
        this.binancePriceProvider = binancePriceProvider;
        this.priceEventPublisher = priceEventPublisher;
    }

    @Scheduled(fixedRateString = "${price.stock.interval:10000}")
    public void updateStockPrices() {
        log.info("Running stock price simulation...");
        Set<String> symbols = getSymbols(Stock.class);
        symbols.forEach(symbol -> fetchAndPublish(symbol, AssetType.STOCK, simulatedPriceProvider));
    }

    @Scheduled(fixedRateString = "${price.crypto.interval:2000}")
    public void updateCryptoPrices() {
        log.info("Fetching crypto prices from Binance...");
        Set<String> symbols = getSymbols(Crypto.class);
        symbols.forEach(symbol -> fetchAndPublish(symbol, AssetType.CRYPTO, binancePriceProvider));
    }

    private Set<String> getSymbols(Class<? extends Asset> type) {
        return assetRepository.findByType(type).stream()
                .map(Asset::getSymbol)
                .collect(Collectors.toSet());
    }

    private void fetchAndPublish(String symbol, AssetType assetType, PriceProvider provider) {
        Float price = provider.getPrice(symbol);
        if (price == null) {
            log.warn("Skipping price update for {} — provider returned null", symbol);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        priceHistoryRepository.save(new PriceHistory(symbol, price, now));
        priceEventPublisher.publishPriceUpdate(new PriceUpdateEvent(symbol, price, now, assetType.name()));
        log.debug("Updated price for {}: {}", symbol, price);
    }
}
