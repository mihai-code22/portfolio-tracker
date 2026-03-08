package com.portfolio.tracker.service.pnl;

import com.portfolio.tracker.dto.asset.pnl.AssetPnlDTO;
import com.portfolio.tracker.repository.postgres.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PnlBroadcastService {

    private final AssetRepository assetRepository;
    private final PnlCalculator pnlCalculator;
    private final SimpMessagingTemplate messagingTemplate;

    public PnlBroadcastService(AssetRepository assetRepository, PnlCalculator pnlCalculator,
                                SimpMessagingTemplate messagingTemplate) {
        this.assetRepository = assetRepository;
        this.pnlCalculator = pnlCalculator;
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastForSymbol(String symbol, Float currentPrice) {
        assetRepository.findBySymbolWithPortfolioAndUser(symbol).forEach(asset -> {
            String username = asset.getPortfolio().getUser().getUsername();
            AssetPnlDTO assetPnlDTO = pnlCalculator.forAsset(asset, currentPrice);
            messagingTemplate.convertAndSendToUser(username, "/queue/pnl", assetPnlDTO);
            log.debug("Sent PnL update to user {}: {} → {}", username, symbol, assetPnlDTO.pnl());
        });
    }
}
