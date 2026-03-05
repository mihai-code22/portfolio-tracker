package com.portfolio.tracker.repository.postgres;

import com.portfolio.tracker.entity.postgres.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUserId(Long userId);

    List<Portfolio> findByUserUsername(String username);
}
