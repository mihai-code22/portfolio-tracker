package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PortfolioService {

    PortfolioResponseDTO create(PortfolioRequestDTO portfolioRequestDTO, Long userId);

    PortfolioResponseDTO createForUsername(PortfolioRequestDTO portfolioRequestDTO, String username);

    PortfolioResponseDTO findById(Long id);

    List<PortfolioResponseDTO> findByUserId(Long userId, String username) throws AccessDeniedException;

    List<PortfolioResponseDTO> findByUsername(String username);

    void delete(Long id, String username) throws AccessDeniedException;
}
