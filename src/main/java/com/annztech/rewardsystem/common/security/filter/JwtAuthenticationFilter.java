package com.annztech.rewardsystem.common.security.filter;

import com.annztech.rewardsystem.common.security.config.JwtConfig;
import com.annztech.rewardsystem.common.security.helper.JwtHelper;
import com.annztech.rewardsystem.modules.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;
    public JwtAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token = authHeader.replace("Bearer ", "");
        if (!JwtHelper.validateToken(token, jwtConfig.getSecretKey())) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwtDTO = JwtHelper.getClaimsFromToken(token, jwtConfig.getSecretKey());
        if( jwtDTO.getTokenType().equalsIgnoreCase("refresh_token")){
           filterChain.doFilter(request, response);
           return;
        }
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + jwtDTO.getRole()),
                new SimpleGrantedAuthority("SCOPE_" + jwtDTO.getScope())
        );
        var authentication = new UsernamePasswordAuthenticationToken(jwtDTO, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
