package com.portfolio.tracker.dto.portfolio;

import com.portfolio.tracker.entity.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class PortfolioMapper {

    public Portfolio toEntity(PortfolioRequestDTO portfolioRequestDTO) {
        return Portfolio.builder()
                .name(portfolioRequestDTO.name())
                .build();
    }

    public PortfolioResponseDTO toDto(Portfolio portfolio) {
        return new PortfolioResponseDTO(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getCreatedAt().toLocalDate(),
                portfolio.getUser().getId()
        );
    }
}
