package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.user.UserMapper;
import com.portfolio.tracker.dto.user.UserRequestDTO;
import com.portfolio.tracker.dto.user.UserResponseDTO;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.exception.DuplicateResourceException;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

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
}
