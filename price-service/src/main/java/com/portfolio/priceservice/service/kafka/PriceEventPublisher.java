package com.portfolio.priceservice.service.kafka;

import com.portfolio.common.event.PriceUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceEventPublisher {

    private final KafkaTemplate<String, PriceUpdateEvent> kafkaTemplate;
    private final String priceUpdatesTopic;

    public PriceEventPublisher(KafkaTemplate<String, PriceUpdateEvent> kafkaTemplate,
                               @Value("${kafka.topics.price-updates.name}") String priceUpdatesTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.priceUpdatesTopic = priceUpdatesTopic;
    }

    public void publishPriceUpdate(PriceUpdateEvent event) {
        kafkaTemplate.send(priceUpdatesTopic, event.symbol(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish price update for {}: {}", event.symbol(), ex.getMessage());
                    } else {
                        log.debug("Published price update for {}: {} offset: {}",
                                event.symbol(),
                                event.price(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
