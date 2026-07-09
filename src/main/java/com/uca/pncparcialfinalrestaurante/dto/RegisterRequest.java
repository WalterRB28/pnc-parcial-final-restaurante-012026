package com.uca.pncparcialfinalrestaurante.dto;

import com.uca.pncparcialfinalrestaurante.entity.enums.Role;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private Set<Role> roles;
    private Long sucursalId; // opcional para encargado
}