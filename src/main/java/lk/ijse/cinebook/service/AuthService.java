package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.LoginRequest;
import lk.ijse.cinebook.dto.request.RegisterRequest;
import lk.ijse.cinebook.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
