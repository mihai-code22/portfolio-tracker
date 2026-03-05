package com.portfolio.tracker.service.user;

import com.portfolio.tracker.dto.login.LoginRequestDTO;
import com.portfolio.tracker.dto.login.LoginResponseDTO;
import com.portfolio.tracker.dto.user.UserRequestDTO;
import com.portfolio.tracker.dto.user.UserResponseDTO;
import com.portfolio.tracker.entity.postgres.User;
import com.portfolio.tracker.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp implements LoginService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public LoginServiceImp(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public LoginResponseDTO register(UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.create(userRequestDTO);
        return new LoginResponseDTO(jwtUtil.generateToken(userResponseDTO.username()));
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String password = loginRequestDTO.password();
        String username = loginRequestDTO.username();
        User user = userService.findEntityByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new LoginResponseDTO(jwtUtil.generateToken(username));
    }
}
