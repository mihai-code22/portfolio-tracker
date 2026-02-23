package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.UserRequestDTO;
import com.portfolio.tracker.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO create(UserRequestDTO user);

    UserResponseDTO findById(Long id);

    UserResponseDTO findByUsername(String username);
}
