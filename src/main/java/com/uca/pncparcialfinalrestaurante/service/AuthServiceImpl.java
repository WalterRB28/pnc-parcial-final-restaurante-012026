package com.uca.pncparcialfinalrestaurante.service.impl;

import com.uca.pncparcialfinalrestaurante.dto.*;
import com.uca.pncparcialfinalrestaurante.entity.*;
import com.uca.pncparcialfinalrestaurante.repository.*;
import com.uca.pncparcialfinalrestaurante.security.JwtUtil;
import com.uca.pncparcialfinalrestaurante.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class AuthServiceImpl implements com.uca.pncparcialfinalrestaurante.service.AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SucursalRepository sucursalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UsuarioRepository usuarioRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           SucursalRepository sucursalRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.sucursalRepository = sucursalRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario u = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        String accessToken = jwtUtil.generateAccessToken(u.getUsername(), u.getId(), u.getRoles());

        // generate refresh token string and store hashed
        String refreshTokenPlain = jwtUtil.generateRefreshTokenString();
        String tokenHash = HashUtil.sha256Hex(refreshTokenPlain);

        RefreshToken rt = RefreshToken.builder()
                .tokenHash(tokenHash)
                .usuario(u)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();
        refreshTokenRepository.save(rt);

        return new AuthResponse(accessToken, refreshTokenPlain, jwtUtil.getAccessTokenExpirationMs(), refreshTokenExpirationMs);
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        String tokenPlain = request.getRefreshToken();
        String tokenHash = HashUtil.sha256Hex(tokenPlain);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (stored.isRevoked() || Instant.now().isAfter(stored.getExpiresAt())) {
            throw new IllegalArgumentException("Refresh token expirado o revocado");
        }

        Usuario u = stored.getUsuario();

        // rotate refresh token: delete old, create new
        refreshTokenRepository.delete(stored);

        String newRefreshPlain = jwtUtil.generateRefreshTokenString();
        String newHash = HashUtil.sha256Hex(newRefreshPlain);

        RefreshToken newRt = RefreshToken.builder()
                .tokenHash(newHash)
                .usuario(u)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRt);

        String newAccess = jwtUtil.generateAccessToken(u.getUsername(), u.getId(), u.getRoles());

        return new AuthResponse(newAccess, newRefreshPlain, jwtUtil.getAccessTokenExpirationMs(), refreshTokenExpirationMs);
    }

    @Override
    public void register(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        Usuario u = new Usuario();
        u.setUsername(request.getUsername());
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setRoles(request.getRoles());
        if (request.getSucursalId() != null) {
            Sucursal s = sucursalRepository.findById(request.getSucursalId())
                    .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
            u.setSucursal(s);
        }
        usuarioRepository.save(u);
    }
}