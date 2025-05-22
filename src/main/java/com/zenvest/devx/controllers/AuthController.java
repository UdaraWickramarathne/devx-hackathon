package com.zenvest.devx.controllers;


import com.zenvest.devx.constants.ApiEndpoint;
import com.zenvest.devx.dtos.inputs.LoginRequest;
import com.zenvest.devx.dtos.inputs.RegisterRequest;
import com.zenvest.devx.dtos.inputs.TokenRefreshRequest;
import com.zenvest.devx.dtos.outputs.TokenResponse;
import com.zenvest.devx.dtos.outputs.UserResponse;
import com.zenvest.devx.responses.ZenvestResponse;
import com.zenvest.devx.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController handles all authentication-related operations.
 * It provides endpoints for user registration, login, and token refresh.
 */
@RestController
@RequestMapping(ApiEndpoint.AUTH)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user.
     *
     * @param request the request object containing user registration details
     * @return a ResponseEntity containing a ZenvestResponse with the created UserResponse object
     */
@PostMapping(ApiEndpoint.AUTH_REGISTER)
    public ResponseEntity<ZenvestResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        ZenvestResponse<UserResponse> response = new ZenvestResponse<>(userResponse);
        response.setMessage("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /**
     * Authenticates a user and generates a token.
     *
     * @param request the request object containing user login details
     * @return a ResponseEntity containing a ZenvestResponse with the generated TokenResponse object
     */
   @PostMapping(ApiEndpoint.AUTH_LOGIN)
    public ResponseEntity<ZenvestResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.authenticateUser(request);
        ZenvestResponse<TokenResponse> response = new ZenvestResponse<>(tokenResponse);
        response.setMessage("User logged in successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Refreshes the authentication token.
     *
     * @param request the request object containing the refresh token
     * @return a ResponseEntity containing a ZenvestResponse with the new TokenResponse object
     */
    @PostMapping(ApiEndpoint.AUTH_REFRESH)
    public ResponseEntity<ZenvestResponse<TokenResponse>> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            TokenResponse refreshResponse = authService.refreshToken(request.getRefreshToken());
            ZenvestResponse<TokenResponse> response = new ZenvestResponse<>(refreshResponse);
            response.setMessage("Refresh token completed successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ZenvestResponse<>(false, null, e.getMessage()));
        }
    }
}
