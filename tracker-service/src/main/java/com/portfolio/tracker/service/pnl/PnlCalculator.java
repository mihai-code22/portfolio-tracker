package com.portfolio.tracker.service.pnl;

import com.portfolio.tracker.dto.asset.AssetMapper;
import com.portfolio.tracker.dto.asset.pnl.AssetPnlDTO;
import com.portfolio.tracker.dto.portfolio.pnl.PortfolioPnlDTO;
import com.portfolio.tracker.entity.postgres.Asset;
import com.portfolio.tracker.entity.postgres.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PnlCalculator {

    private final AssetMapper assetMapper;

    public AssetPnlDTO forAsset(Asset asset, Float currentPrice) {
        Float pnl = null;
        Float pnlPercentage = null;

        if (currentPrice != null) {
            pnl = (currentPrice - asset.getBuyPrice()) * asset.getQuantity();
            pnlPercentage = ((currentPrice - asset.getBuyPrice()) / asset.getBuyPrice()) * 100;
        }

        return assetMapper.toPnlDto(asset, currentPrice, pnl, pnlPercentage);
    }

    public PortfolioPnlDTO forPortfolio(Portfolio portfolio, List<AssetPnlDTO> assetPnls) {
        float totalInvested = 0f;
        float currentValue = 0f;

        for (AssetPnlDTO asset : assetPnls) {
            if (asset.currentPrice() != null) {
                totalInvested += asset.buyPrice() * asset.quantity();
                currentValue += asset.currentPrice() * asset.quantity();
            }
        }

        float pnl = currentValue - totalInvested;
        float pnlPercentage = totalInvested != 0 ? (pnl / totalInvested) * 100 : 0f;

        return new PortfolioPnlDTO(
                portfolio.getId(),
                portfolio.getName(),
                totalInvested,
                currentValue,
                pnl,
                pnlPercentage
        );
    }
}
