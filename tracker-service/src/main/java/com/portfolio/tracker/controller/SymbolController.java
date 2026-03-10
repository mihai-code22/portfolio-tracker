package com.portfolio.tracker.controller;

import com.portfolio.common.enums.AssetType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/symbols")
public class SymbolController {

    private static final Map<AssetType, List<String>> SUPPORTED_SYMBOLS = Map.of(
            AssetType.CRYPTO, List.of("BTC", "ETH", "SOL", "BNB", "XRP", "ADA", "DOGE", "MATIC"),
            AssetType.STOCK, List.of("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "NVDA", "META", "NFLX"),
            AssetType.BOND, List.of("ROGOV2027", "ROGOV2030", "AAPL2026", "MSFT2027", "GOOGL2028", "USTSY2026", "USTSY2030")
    );

    @GetMapping
    public ResponseEntity<List<String>> getSymbols(@RequestParam AssetType type) {
        List<String> symbols = SUPPORTED_SYMBOLS.getOrDefault(type, List.of());
        return ResponseEntity.ok(symbols);
    }
}
