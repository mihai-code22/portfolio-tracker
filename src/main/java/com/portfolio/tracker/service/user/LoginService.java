package com.portfolio.tracker.service.user;

import com.portfolio.tracker.dto.login.LoginRequestDTO;
import com.portfolio.tracker.dto.login.LoginResponseDTO;
import com.portfolio.tracker.dto.user.UserRequestDTO;

public interface LoginService {

    LoginResponseDTO register(UserRequestDTO userRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
