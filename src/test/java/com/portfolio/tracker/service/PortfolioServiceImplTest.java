package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.portfolio.PortfolioMapper;
import com.portfolio.tracker.dto.portfolio.PortfolioRequestDTO;
import com.portfolio.tracker.dto.portfolio.PortfolioResponseDTO;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.PortfolioRepository;
import com.portfolio.tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioMapper portfolioMapper;

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    private User user;
    private Portfolio portfolio;
    private PortfolioRequestDTO requestDTO;
    private PortfolioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("encodedPassword")
                .build();

        portfolio = Portfolio.builder()
                .id(10L)
                .name("My Portfolio")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        requestDTO = new PortfolioRequestDTO("My Portfolio");
        responseDTO = new PortfolioResponseDTO(10L, "My Portfolio", LocalDate.now(), 1L);
    }

    @Test
    void create_shouldSavePortfolioAndReturnDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(portfolioMapper.toEntity(requestDTO)).thenReturn(portfolio);
        when(portfolioRepository.save(portfolio)).thenReturn(portfolio);
        when(portfolioMapper.toDto(portfolio)).thenReturn(responseDTO);

        PortfolioResponseDTO result = portfolioService.create(requestDTO, 1L);

        assertThat(result).isEqualTo(responseDTO);
        verify(portfolioRepository).save(portfolio);
        assertThat(portfolio.getUser()).isEqualTo(user);
    }

    @Test
    void create_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> portfolioService.create(requestDTO, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(portfolioRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnDTO_whenPortfolioExists() {
        when(portfolioRepository.findById(10L)).thenReturn(Optional.of(portfolio));
        when(portfolioMapper.toDto(portfolio)).thenReturn(responseDTO);

        PortfolioResponseDTO result = portfolioService.findById(10L);

        assertThat(result).isEqualTo(responseDTO);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenPortfolioNotFound() {
        when(portfolioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> portfolioService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Portfolio not found");
    }

    @Test
    void findByUserId_shouldReturnDTOList_whenUserMatchesAndExists() throws AccessDeniedException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(portfolioRepository.findByUserId(1L)).thenReturn(List.of(portfolio));
        when(portfolioMapper.toDto(portfolio)).thenReturn(responseDTO);

        List<PortfolioResponseDTO> result = portfolioService.findByUserId(1L, "johndoe");

        assertThat(result).hasSize(1).containsExactly(responseDTO);
    }

    @Test
    void findByUserId_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> portfolioService.findByUserId(99L, "johndoe"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void findByUserId_shouldThrowAccessDeniedException_whenUsernameDoesNotMatch() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> portfolioService.findByUserId(1L, "otheruser"))
                .isInstanceOf(AccessDeniedException.class);

        verify(portfolioRepository, never()).findByUserId(any());
    }

    @Test
    void delete_shouldDeletePortfolio_whenPortfolioExistsAndUsernameMatches() throws AccessDeniedException {
        when(portfolioRepository.findById(10L)).thenReturn(Optional.of(portfolio));

        portfolioService.delete(10L, "johndoe");

        verify(portfolioRepository).delete(portfolio);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenPortfolioNotFound() {
        when(portfolioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> portfolioService.delete(99L, "johndoe"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Portfolio not found");

        verify(portfolioRepository, never()).delete(any());
    }

    @Test
    void delete_shouldThrowAccessDeniedException_whenUsernameDoesNotMatch() {
        when(portfolioRepository.findById(10L)).thenReturn(Optional.of(portfolio));

        assertThatThrownBy(() -> portfolioService.delete(10L, "otheruser"))
                .isInstanceOf(AccessDeniedException.class);

        verify(portfolioRepository, never()).delete(any());
    }
}
