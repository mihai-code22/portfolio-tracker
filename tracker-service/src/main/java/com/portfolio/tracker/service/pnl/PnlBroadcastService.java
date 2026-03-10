package com.portfolio.tracker.service.pnl;

import com.portfolio.common.enums.AssetType;
import com.portfolio.tracker.dto.asset.pnl.AssetPnlDTO;
import com.portfolio.tracker.entity.postgres.Asset;
import com.portfolio.tracker.repository.postgres.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PnlBroadcastService {

    private final AssetRepository assetRepository;
    private final PnlCalculator pnlCalculator;
    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastForSymbol(String symbol, Float currentPrice) {
        assetRepository.findBySymbolWithPortfolioAndUser(symbol).forEach(asset -> {
            String username = asset.getPortfolio().getUser().getUsername();
            AssetPnlDTO assetPnlDTO = pnlCalculator.forAsset(asset, currentPrice);
            messagingTemplate.convertAndSendToUser(username, "/queue/pnl", assetPnlDTO);
            log.debug("Sent PnL update to user {}: {} → {}", username, symbol, assetPnlDTO.pnl());
        });
    }
}
