package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-03-2025
 */

import com.brainqy.api.constants.RequestStatusTypes;
import com.brainqy.api.constants.UserAccountStatusTypes;
import com.brainqy.api.dto.DailyStreakDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.security.jwt.JwtAuthRequest;
import com.brainqy.api.security.jwt.JwtAuthResponse;
import com.brainqy.api.security.jwt.JwtTokenHelper;
import com.brainqy.api.security.userdetails.CustomUserDetails;
import com.brainqy.api.security.userdetails.CustomUserDetailsServiceImpl;
import com.brainqy.api.services.IServices.IDailyStreakService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IAuthServiceImplTest {

    @Mock
    private JwtTokenHelper tokenHelper;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IYtmsUserService userService;

    @Mock
    private IDailyStreakService dailyStreakService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private IAuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldFailWithEmptyEmailAndPassword() {
        JwtAuthRequest authRequest = new JwtAuthRequest("", "");

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            authService.login(authRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void login_ShouldFailWithNullEmailAndPassword() {
        JwtAuthRequest authRequest = new JwtAuthRequest(null, null);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            authService.login(authRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void login_ShouldFailWithInvalidEmailFormat() {
        JwtAuthRequest authRequest = new JwtAuthRequest("invalid-email", "password");

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            authService.login(authRequest);
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void login_ShouldFailWhenAccountStatusIsPending() {
        JwtAuthRequest authRequest = new JwtAuthRequest("gahininathdhakane2@gmail.com", "password");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getAccountStatus()).thenReturn(UserAccountStatusTypes.PENDING);
        when(userDetailsService.loadUserByUsername("gahininathdhakane2@gmail.com")).thenReturn(userDetails);

        authService.authenticate("gahininathdhakane2@gmail.com", "password");

        JwtAuthResponse response = authService.login(authRequest);

        assertNull(response.getToken());
        assertEquals(RequestStatusTypes.FAILED.toString(), response.getStatus());
        assertEquals("Your request is in Pending state for Approval", response.getMessage());
    }

    @Test
    void login_ShouldFailWhenAccountStatusIsDeclined() {
        JwtAuthRequest authRequest = new JwtAuthRequest("gahininathdhakane@gmail.com", "password");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getAccountStatus()).thenReturn(UserAccountStatusTypes.DECLINED);
        when(userDetailsService.loadUserByUsername("gahininathdhakane@gmail.com")).thenReturn(userDetails);

        authService.authenticate("gahininathdhakane@gmail.com", "password");

        JwtAuthResponse response = authService.login(authRequest);

        assertNull(response.getToken());
        assertEquals(RequestStatusTypes.FAILED.toString(), response.getStatus());
        assertEquals("Your Request is Declined by Admin !", response.getMessage());
    }
}
