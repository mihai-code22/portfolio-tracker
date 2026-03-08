package com.portfolio.tracker.service.portfolio;

import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;
import com.portfolio.tracker.dto.portfolio.pnl.PortfolioPnlDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PortfolioService {

    PortfolioResponseDTO create(PortfolioRequestDTO portfolioRequestDTO, Long userId);

    PortfolioResponseDTO createForUsername(PortfolioRequestDTO portfolioRequestDTO, String username);

    PortfolioResponseDTO findById(Long id);

    List<PortfolioResponseDTO> findByUserId(Long userId, String username) throws AccessDeniedException;

    List<PortfolioResponseDTO> findByUsername(String username);

    List<PortfolioPnlDTO> findByUsernameWithPnl(String username);

    void delete(Long id, String username) throws AccessDeniedException;
}
