package com.example.secureapp.service;

import com.example.secureapp.dto.JwtAuthenticationResponse;
import com.example.secureapp.dto.SignInRequest;
import com.example.secureapp.dto.SignUpRequest;
import com.example.secureapp.model.Role;
import com.example.secureapp.model.User;
import com.example.secureapp.repository.UserRepository;
import com.example.secureapp.security.JwtTokenProvider;
import com.example.secureapp.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setPassword("password");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .password("encodedPassword")
                .roles(Set.of(Role.USER))
                .build();
    }

    @Test
    void signUp_shouldCreateUser_whenUsernameAndEmailAreAvailable() {
        when(userRepository.findByUsername(signUpRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> authenticationService.signUp(signUpRequest));
    }

    @Test
    void signUp_shouldThrowException_whenUsernameIsTaken() {
        when(userRepository.findByUsername(signUpRequest.getUsername())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authenticationService.signUp(signUpRequest));
    }

    @Test
    void signUp_shouldThrowException_whenEmailIsTaken() {
        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authenticationService.signUp(signUpRequest));
    }

    @Test
    void signIn_shouldReturnJwt_whenCredentialsAreValid() {
        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(any(UserPrincipal.class))).thenReturn("test-token");

        JwtAuthenticationResponse response = authenticationService.signIn(signInRequest);

        assertNotNull(response);
        assertEquals("test-token", response.getAccessToken());
    }
}