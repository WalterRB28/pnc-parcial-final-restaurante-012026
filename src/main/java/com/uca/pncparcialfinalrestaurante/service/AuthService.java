package com.uca.pncparcialfinalrestaurante.service;

import com.uca.pncparcialfinalrestaurante.dto.*;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshRequest request);
    void register(RegisterRequest request);
    void changePassword(ChangePasswordRequest request, String username);
}