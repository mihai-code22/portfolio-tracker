package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.user.UserMapper;
import com.portfolio.tracker.dto.user.UserRequestDTO;
import com.portfolio.tracker.dto.user.UserResponseDTO;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.exception.DuplicateResourceException;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO("johndoe", "john@example.com", "Password1");
        user = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("Password1")
                .build();
        userResponseDTO = new UserResponseDTO(1L, "johndoe", "john@example.com", LocalDateTime.now());
    }

    @Test
    void create_shouldSaveUserAndReturnDTO() {
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password1")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.create(userRequestDTO);

        assertThat(result).isEqualTo(userResponseDTO);
        verify(passwordEncoder).encode("Password1");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(user);
    }

    @Test
    void create_shouldThrowDuplicateResourceException_whenUsernameAlreadyExists() {
        when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.create(userRequestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Username already exists");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void findById_shouldReturnDTO_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.findById(1L);

        assertThat(result).isEqualTo(userResponseDTO);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void findByUsername_shouldReturnDTO_whenUserExists() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.findByUsername("johndoe");

        assertThat(result).isEqualTo(userResponseDTO);
    }

    @Test
    void findByUsername_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void findEntityByUsername_shouldReturnUserEntity_whenUserExists() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        User result = userService.findEntityByUsername("johndoe");

        assertThat(result).isEqualTo(user);
        assertThat(result.getUsername()).isEqualTo("johndoe");
    }

    @Test
    void findEntityByUsername_shouldThrowResourceNotFoundException_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findEntityByUsername("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }
}
