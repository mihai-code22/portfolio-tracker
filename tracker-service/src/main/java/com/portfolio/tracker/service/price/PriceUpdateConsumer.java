package com.portfolio.tracker.service.price;

import com.portfolio.common.event.PriceUpdateEvent;
import com.portfolio.tracker.service.pnl.PnlBroadcastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceUpdateConsumer {

    private final PriceCache priceCache;
    private final PnlBroadcastService pnlBroadcastService;

    public PriceUpdateConsumer(PriceCache priceCache, PnlBroadcastService pnlBroadcastService) {
        this.priceCache = priceCache;
        this.pnlBroadcastService = pnlBroadcastService;
    }

    @KafkaListener(
            topics = "${kafka.topics.price-updates.name}",
            groupId = "${kafka.consumer.group-id}"
    )
    public void consume(PriceUpdateEvent event) {
        log.debug("Received price update: {} → {}", event.symbol(), event.price());
        priceCache.update(event.symbol(), event.price());
        pnlBroadcastService.broadcastForSymbol(event.symbol(), event.price());
    }
}
