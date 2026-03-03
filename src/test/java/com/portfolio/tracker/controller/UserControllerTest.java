package com.portfolio.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.tracker.dto.user.UserRequestDTO;
import com.portfolio.tracker.dto.user.UserResponseDTO;
import com.portfolio.tracker.exception.DuplicateResourceException;
import com.portfolio.tracker.exception.ResourceNotFoundException;
import com.portfolio.tracker.security.JwtUtil;
import com.portfolio.tracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserResponseDTO userResponseDTO =
            new UserResponseDTO(1L, "johndoe", "john@example.com", LocalDateTime.now());

    @Test
    @WithMockUser
    void create_shouldReturn201_whenRequestIsValid() throws Exception {
        UserRequestDTO request = new UserRequestDTO("johndoe", "john@example.com", "Password1");
        when(userService.create(any())).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser
    void create_shouldReturn400_whenRequestIsInvalid() throws Exception {
        UserRequestDTO invalid = new UserRequestDTO("jo", "not-an-email", "weak");

        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCause").isNotEmpty());
    }

    @Test
    @WithMockUser
    void create_shouldReturn409_whenUsernameAlreadyExists() throws Exception {
        UserRequestDTO request = new UserRequestDTO("johndoe", "john@example.com", "Password1");
        when(userService.create(any())).thenThrow(new DuplicateResourceException("Username already exists"));

        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCause").value("Username already exists"));
    }

    @Test
    void create_shouldReturn401_whenNotAuthenticated() throws Exception {
        UserRequestDTO request = new UserRequestDTO("johndoe", "john@example.com", "Password1");

        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getById_shouldReturn200_whenUserExists() throws Exception {
        when(userService.findById(1L)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser
    void getById_shouldReturn404_whenUserNotFound() throws Exception {
        when(userService.findById(99L)).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCause").value("User not found"));
    }

    @Test
    void getById_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getByUsername_shouldReturn200_whenUserExists() throws Exception {
        when(userService.findByUsername("johndoe")).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/v1/users/username/johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser
    void getByUsername_shouldReturn404_whenUserNotFound() throws Exception {
        when(userService.findByUsername("unknown")).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/username/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCause").value("User not found"));
    }

    @Test
    void getByUsername_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users/username/johndoe"))
                .andExpect(status().isUnauthorized());
    }
}
