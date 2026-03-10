package com.portfolio.priceservice.service.price.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component("binance")
@Slf4j
public class BinancePriceProvider implements PriceProvider {

    private static final String BINANCE_TICKER_URL = "https://api.binance.com/api/v3/ticker/price?symbol={symbol}USDT";

    private final RestClient restClient;

    public BinancePriceProvider(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Float getPrice(String symbol) {
        try {
            Map<String, String> response = restClient.get()
                    .uri(BINANCE_TICKER_URL, symbol)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("price")) {
                log.debug("Price fetch from Binance for {} : {}", symbol, response.get("price"));
                return Float.parseFloat(response.get("price"));
            }

            log.warn("No price returned from Binance for symbol: {}", symbol);
            return null;
        } catch (Exception e) {
            log.error("Failed to fetch price from Binance for symbol: {}", symbol, e);
            return null;
        }
    }
}
