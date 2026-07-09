package com.uca.pncparcialfinalrestaurante.security;

import com.uca.pncparcialfinalrestaurante.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService, UsuarioRepository usuarioRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (jwtUtil.validateAccessToken(token)) {
                    String username = jwtUtil.getUsernameFromAccessToken(token);
                    Integer tokenVersionFromJwt = jwtUtil.getTokenVersionFromAccessToken(token);

                    // Validar que tokenVersion coincida con la BD
                    var usuarioOpt = usuarioRepository.findByUsername(username);
                    if (usuarioOpt.isPresent()) {
                        var usuario = usuarioOpt.get();
                        if (tokenVersionFromJwt == null || !tokenVersionFromJwt.equals(usuario.getTokenVersion())) {
                            // Token inválido por cambio de contraseña
                            chain.doFilter(req, res);
                            return;
                        }
                    }

                    UserDetails ud = userDetailsService.loadUserByUsername(username);
                    var auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // ignore - unauthenticated request will be handled by Spring Security
            }
        }
        chain.doFilter(req, res);
    }
}