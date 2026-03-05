package com.portfolio.tracker.service.user;

import com.portfolio.tracker.dto.user.UserRequestDTO;
import com.portfolio.tracker.dto.user.UserResponseDTO;
import com.portfolio.tracker.entity.postgres.User;

public interface UserService {

    UserResponseDTO create(UserRequestDTO user);

    UserResponseDTO findById(Long id);

    UserResponseDTO findByUsername(String username);

    User findEntityByUsername(String username);
}
