package com.uca.pncparcialfinalrestaurante.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresInMs;
    private long refreshTokenExpiresInMs;
}