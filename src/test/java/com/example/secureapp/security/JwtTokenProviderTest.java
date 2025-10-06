package com.example.secureapp.security;

import com.example.secureapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "averylongandsecuresecretkeythatshouldnotbehardcodedinproduction");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationDate", 86400000L);

        User user = User.builder()
                .username("testuser")
                .build();
        userDetails = UserPrincipal.create(user);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtTokenProvider.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtTokenProvider.generateToken(userDetails);
        String username = jwtTokenProvider.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtTokenProvider.generateToken(userDetails);
        assertTrue(jwtTokenProvider.validateToken(token, userDetails));
    }

    @Test
    void validateToken_shouldReturnFalse_whenTokenIsInvalid() {
        String token = jwtTokenProvider.generateToken(userDetails);
        User otherUser = User.builder().username("otheruser").build();
        UserDetails otherUserDetails = UserPrincipal.create(otherUser);
        assertFalse(jwtTokenProvider.validateToken(token, otherUserDetails));
    }
}