package com.uca.pncparcialfinalrestaurante.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequest {
    private String refreshToken;
}