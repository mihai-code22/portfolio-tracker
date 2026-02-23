package com.portfolio.tracker.dto.user;

import com.portfolio.tracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.username())
                .email(userRequestDTO.email())
                .password(userRequestDTO.password())
                .build();
    }

    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
