package com.portfolio.tracker.dto.asset;

import com.portfolio.tracker.dto.asset.request.AssetRequestDTO;
import com.portfolio.tracker.dto.asset.request.BondRequestDTO;
import com.portfolio.tracker.dto.asset.request.CryptoRequestDTO;
import com.portfolio.tracker.dto.asset.request.StockRequestDTO;
import com.portfolio.tracker.dto.asset.response.AssetResponseDTO;
import com.portfolio.tracker.dto.asset.response.BondResponseDTO;
import com.portfolio.tracker.dto.asset.response.CryptoResponseDTO;
import com.portfolio.tracker.dto.asset.response.StockResponseDTO;
import com.portfolio.tracker.entity.postgres.Asset;
import com.portfolio.tracker.entity.postgres.assets.Bond;
import com.portfolio.tracker.entity.postgres.assets.Crypto;
import com.portfolio.tracker.entity.postgres.assets.Stock;
import com.portfolio.tracker.enums.AssetType;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper {

    public Asset toEntity(AssetRequestDTO dto) {
        return switch (dto) {
            case StockRequestDTO r -> Stock.builder()
                    .symbol(r.symbol())
                    .quantity(r.quantity())
                    .buyPrice(r.buyPrice())
                    .exchange(r.exchange())
                    .sector(r.sector())
                    .build();
            case CryptoRequestDTO r -> Crypto.builder()
                    .symbol(r.symbol())
                    .quantity(r.quantity())
                    .buyPrice(r.buyPrice())
                    .blockchain(r.blockchain())
                    .build();
            case BondRequestDTO r -> Bond.builder()
                    .symbol(r.symbol())
                    .quantity(r.quantity())
                    .buyPrice(r.buyPrice())
                    .couponRate(r.couponRate())
                    .maturityDate(r.maturityDate())
                    .build();
        };
    }

    public AssetResponseDTO toDto(Asset asset) {
        return switch (asset) {
            case Stock s -> new StockResponseDTO(
                    s.getId(),
                    s.getSymbol(),
                    s.getQuantity(),
                    s.getBuyPrice(),
                    s.getPortfolio().getId(),
                    s.getExchange(),
                    s.getSector(),
                    AssetType.STOCK.getType()
            );
            case Crypto c -> new CryptoResponseDTO(
                    c.getId(),
                    c.getSymbol(),
                    c.getQuantity(),
                    c.getBuyPrice(),
                    c.getPortfolio().getId(),
                    c.getBlockchain(),
                    AssetType.CRYPTO.getType()
            );
            case Bond b -> new BondResponseDTO(
                    b.getId(),
                    b.getSymbol(),
                    b.getQuantity(),
                    b.getBuyPrice(),
                    b.getPortfolio().getId(),
                    b.getCouponRate(),
                    b.getMaturityDate(),
                    AssetType.BOND.getType()
            );
            default -> throw new IllegalArgumentException("Unknown asset type: " + asset.getClass().getSimpleName());
        };
    }
}
