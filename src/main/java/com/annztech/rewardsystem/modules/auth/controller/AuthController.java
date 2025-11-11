package com.annztech.rewardsystem.modules.auth.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.security.config.JwtConfig;
import com.annztech.rewardsystem.modules.auth.constants.AuthConstants;
import com.annztech.rewardsystem.modules.auth.dto.AuthLoginDTO;
import com.annztech.rewardsystem.modules.auth.dto.AuthResponseDTO;
import com.annztech.rewardsystem.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController is a REST controller that handles authentication-related operations
 * such as user login, token refresh, logout, and fetching user information.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Application Authentication", description = "Supports login, refresh, me endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtConfig jwtConfig;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtConfig = jwtConfig;
    }

    /**
     * Authenticates a user based on the provided login credentials and generates access and refresh tokens.
     * The refresh token is stored in a secure cookie, and the access token is returned in the response body.
     *
     * @param authLoginDTO the login credentials provided by the user, including email and password
     * @param response the HTTP response to which the refresh token cookie will be added
     * @return a ResponseEntity containing a success message, HTTP status, and the access token details
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthLoginDTO authLoginDTO, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLoginDTO.getEmail(), authLoginDTO.getPassword()));
        Pair<AuthResponseDTO, String> pair = authService.getAccessToken(authLoginDTO);
        AuthResponseDTO authResponseDTO = pair.a;
        String refreshToken = pair.b;
        var cookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(cookie);
        return AppResponse.success(AuthConstants.LOGIN_SUCCESS, HttpStatus.OK, authResponseDTO);
    }

    /**
     * Refreshes the user's authentication tokens based on the provided refresh token stored in a secure cookie.
     * This method generates a new pair of access and refresh tokens, stores the new refresh token in a cookie,
     * and returns the updated authentication details in the response body.
     *
     * @param refreshToken the refresh token retrieved from the browser's cookie; may be null if not present
     * @param response the HTTP response to which the updated refresh token cookie will be added
     * @return a ResponseEntity containing a success message, HTTP status, and the updated authentication details including access token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@CookieValue(value=AuthConstants.REFRESH_TOKEN_NAME, required = false) String refreshToken, HttpServletResponse response) {
        Pair<AuthResponseDTO, String> pair = authService.renewRefreshToken(refreshToken);
        AuthResponseDTO authResponseDTO = pair.a;
        String newRefreshToken = pair.b;
        var cookie = createRefreshTokenCookie(newRefreshToken);
        response.addCookie(cookie);
        return AppResponse.success(AuthConstants.TOKEN_REFRESH_SUCCESS, HttpStatus.OK, authResponseDTO);
    }

    /**
     * Logs out the currently authenticated user by clearing the refresh token cookie.
     * The method removes the refresh token from the user's browser to effectively terminate the session.
     *
     * @param response the HTTP response to which the cleared cookie will be added
     * @return a ResponseEntity containing a success message and HTTP status indicating the logout operation was successful
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        var cookie = clearRefreshTokenCookie();
        response.addCookie(cookie);
        return AppResponse.success(AuthConstants.LOGOUT_SUCCESS, HttpStatus.OK, null);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword() {

    }

    @PostMapping("/reset-password")
    public void resetPassword() {}


    @GetMapping("/me")
    public ResponseEntity<Object> getMe() {
        return AppResponse.success(AuthConstants.FETCH_ME_SUCCESS, HttpStatus.OK, authService.getEmployeeDTO()) ;
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        var cookie = new Cookie(AuthConstants.REFRESH_TOKEN_NAME, refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpire().intValue());
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie clearRefreshTokenCookie() {
        var cookie = new Cookie("refreshToken", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        /* Less permssive opening external link cookies will not pass over session*/
        cookie.setAttribute("samesite", "strict");
        /* Less permssive opening external link cookies still persist*/
        //cookie.setAttribute("samesite", "Lax");
        return cookie;
    }
}
