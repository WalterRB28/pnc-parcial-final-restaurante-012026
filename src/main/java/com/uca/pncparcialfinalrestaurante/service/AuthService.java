package com.uca.pncparcialfinalrestaurante.service;

import com.uca.pncparcialfinalrestaurante.dto.AuthResponse;
import com.uca.pncparcialfinalrestaurante.dto.LoginRequest;
import com.uca.pncparcialfinalrestaurante.dto.RegisterRequest;
import com.uca.pncparcialfinalrestaurante.dto.RefreshRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshRequest request);
    void register(RegisterRequest request);
}