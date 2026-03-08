package com.portfolio.priceservice.service.price;

import com.portfolio.priceservice.entity.mongo.PriceHistory;
import com.portfolio.priceservice.entity.postgres.Asset;
import com.portfolio.priceservice.enums.AssetType;
import com.portfolio.priceservice.event.PriceUpdateEvent;
import com.portfolio.priceservice.repository.mongo.PriceHistoryRepository;
import com.portfolio.priceservice.repository.postgres.AssetRepository;
import com.portfolio.priceservice.service.kafka.PriceEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceSimulationService {

    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceProvider priceProvider;
    private final PriceEventPublisher priceEventPublisher;

    public PriceSimulationService(AssetRepository assetRepository,
                                  PriceHistoryRepository priceHistoryRepository,
                                  @Qualifier("simulated") PriceProvider priceProvider, PriceEventPublisher priceEventPublisher) {
        this.assetRepository = assetRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceProvider = priceProvider;
        this.priceEventPublisher = priceEventPublisher;
    }

    @Scheduled(fixedRateString = "${price.simulation.interval:10000}")
    public void simulatePrices() {
        log.info("Running price simulation...");

        List<Asset> assets = assetRepository.findAll();

        Map<AssetType, Set<String>> symbolsByType = assets.stream()
                .collect(Collectors.groupingBy(
                        Asset::getAssetType,
                        Collectors.mapping(Asset::getSymbol, Collectors.toSet())
                ));

        symbolsByType.forEach((assetType, symbols) ->
                symbols.forEach(symbol -> {
                    Float price = priceProvider.getPrice(symbol, assetType);
                    priceHistoryRepository.save(
                            new PriceHistory(symbol, price, LocalDateTime.now())
                    );

                    priceEventPublisher.publishPriceUpdate(
                            new PriceUpdateEvent(symbol, price, LocalDateTime.now(), assetType.name())
                    );

                    log.debug("Updated price for {}: {}", symbol, price);
                })
        );
    }
}
