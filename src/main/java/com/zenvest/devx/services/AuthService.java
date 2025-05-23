package com.zenvest.devx.services;

import com.zenvest.devx.dtos.inputs.LoginRequest;
import com.zenvest.devx.dtos.inputs.RegisterRequest;
import com.zenvest.devx.dtos.outputs.TokenResponse;
import com.zenvest.devx.dtos.outputs.UserResponse;
import com.zenvest.devx.models.User;
import com.zenvest.devx.repositories.UserRepository;
import com.zenvest.devx.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

/**
 * AuthService handles all authentication-related operations.
 * It provides methods for user registration, login, and token management.
 */
@Service
public class AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public AuthService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user.
     * This method is responsible for registering a new user. It checks if the password is valid, if the username and email are already in use,
     * and if not, it creates a new User object, sets its properties, and returns a UserResponse object with the user's details.
     * And user passwords need to be encrypted using BCryptPasswordEncoder
     *
     * @param registerRequest the registration request containing user details
     * @return the registered user's response
     *
     */
    public UserResponse registerUser(RegisterRequest registerRequest) {
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username is already in used");
        }

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email is already in used");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUsername(registerRequest.getUsername());

        user = userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();

    }

    /**
     * Authenticates a user.
     * This method is responsible for authenticating a user. It checks if the user exists in the database,
     * verifies the password, and if valid, generates a JWT token for the user
     *
     * @param request the login request containing user credentials
     * @return the token response containing the JWT token
     */
    public TokenResponse authenticateUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String accessToken = jwtService.buildToken(user.getUsername());
        return new TokenResponse(accessToken);
    }

    /**
     * Refreshes the JWT token.
     * This method is responsible for refreshing the JWT token. It checks if the refresh token is valid,
     * and if so, generates a new access token for the user
     *
     * @param refreshToken the refresh token to be validated
     * @return the token response containing the new access token
     */
    public TokenResponse refreshToken(String refreshToken) throws AuthenticationException {
        boolean isValid = jwtService.isTokenValid(refreshToken);
        if (isValid) {
            String username = jwtService.extractUsername(refreshToken);
            String newToken = jwtService.buildToken(username);
            return new TokenResponse(newToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    /**
     * Retrieves the currently authenticated user.
     * This method is responsible for retrieving the currently authenticated user from the security context.
     *
     * @return the currently authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
