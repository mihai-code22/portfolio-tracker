package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;

import java.util.List;

public interface PortfolioService {

    PortfolioResponseDTO create(PortfolioRequestDTO portfolioRequestDTO, Long userId);

    PortfolioResponseDTO findById(Long id);

    List<PortfolioResponseDTO> findByUserId(Long userId);

    void delete(Long id);
}
