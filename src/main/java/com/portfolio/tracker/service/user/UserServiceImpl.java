package com.portfolio.tracker.service.user;

import com.portfolio.tracker.dto.user.UserMapper;
import com.portfolio.tracker.dto.user.UserRequestDTO;
import com.portfolio.tracker.dto.user.UserResponseDTO;
import com.portfolio.tracker.entity.postgres.User;
import com.portfolio.tracker.exception.DuplicateResourceException;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.postgres.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Created user with id {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDTO findById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserResponseDTO findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
