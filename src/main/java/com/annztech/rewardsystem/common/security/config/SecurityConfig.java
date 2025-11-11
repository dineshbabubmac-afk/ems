package com.annztech.rewardsystem.common.security.config;

import com.annztech.rewardsystem.common.exception.AppException;
import com.annztech.rewardsystem.common.security.filter.JwtAuthenticationFilter;
import com.annztech.rewardsystem.common.security.handler.AppAccessDeniedHandler;
import com.annztech.rewardsystem.common.security.handler.AppAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
        try {
            http
                    .sessionManagement((c) ->
                            c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((a) -> a
                            .requestMatchers(HttpMethod.GET, "/files/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/internal/invites/**").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/internal/invites/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/certificate/redirect").permitAll()
                            .requestMatchers(HttpMethod.GET, "/certificate/redirect/me").permitAll()
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/redoc.html",
                                    "/interactive-redoc.html",
                                    "/api-docs.html",
                                    "/rapidoc.html",
                                    "/docs/**",
                                    "/redoc/**",
                                    "/*.html",
                                    "/*.js",
                                    "/*.css"
                            ).permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/activate").access(this::hasAdmins)
                            .requestMatchers(HttpMethod.PATCH, "/deactivate").access(this::hasAdmins)
                            .anyRequest().authenticated())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(new AppAuthenticationEntryPoint())
                    .accessDeniedHandler(new AppAccessDeniedHandler())
            );
            return http.build();
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private AuthorizationDecision hasAdmins(Supplier<Authentication> authenticationSupplier,
                                                       RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();
        boolean hasValidRole =
                authentication.getAuthorities().stream().anyMatch(auth ->
                        auth.getAuthority().equals("ROLE_") ||
                                auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SUPER_ADMIN")
                );
        return new AuthorizationDecision(hasValidRole );
    }
}
