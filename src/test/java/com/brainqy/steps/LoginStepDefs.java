package com.brainqy.steps;

import com.brainqy.api.constants.RequestStatusTypes;
import com.brainqy.api.constants.UserAccountStatusTypes;
import com.brainqy.api.domain.UserRole;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.UserRoleDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.security.jwt.JwtAuthRequest;
import com.brainqy.api.security.jwt.JwtAuthResponse;
import com.brainqy.api.security.jwt.JwtTokenHelper;
import com.brainqy.api.security.userdetails.CustomUserDetails;
import com.brainqy.api.services.IServices.IAuthService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Step Definitions for Login feature.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Required to use non-static @BeforeAll
public class LoginStepDefs {

    @Mock
    private IAuthService authService;

    @Mock
    private IYtmsUserService userService;

    @Mock
    private JwtTokenHelper tokenHelper;
    @Autowired
    private ModelMapper mapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetails userDetails;

    private JwtAuthRequest authRequest;
    private JwtAuthResponse authResponse;
    private String generatedToken;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("*********  In setup method");
        YtmsUserDto ytmsUser = new YtmsUserDto();
        ytmsUser.setFullName("Dnyanesh Somwanshi");
        ytmsUser.setEmailAdd("dvsomwanshi@gmail.com");
        ytmsUser.setPassword("dnyanesh@123");
        ytmsUser.setAccountStatus(UserAccountStatusTypes.APPROVED);

        UserRoleDto userRole = new UserRoleDto();
        userRole.setRoleTypes("ROLE_USER");
        ytmsUser.setUserRole(userRole);
        userDetails = spy(new CustomUserDetails(mapper.map(ytmsUser,YtmsUser.class)));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, "dnyanesh@123", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock authentication process
        doReturn(authentication).when(authenticationManager).authenticate(any(Authentication.class));
        doReturn("Dnyanesh Somwanshi").when(userDetails).getFullName();
        doReturn("dvsomwanshi@gmail.com").when(userDetails).getEmailAdd();
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).when(userDetails).getAuthorities();

        // Mock JWT Token
        tokenHelper = mock(JwtTokenHelper.class);
        generatedToken = "mocked-jwt-token";
        when(tokenHelper.generateToken(any(CustomUserDetails.class))).thenReturn(generatedToken);
    }

    @Given("a user with email {string} and password {string}")
    public void a_user_with_email_and_password(String email, String password) {
        authRequest = new JwtAuthRequest();
        authRequest.setEmail(email);
        authRequest.setPassword(password);

        // Mock user retrieval
        YtmsUserDto mockUserDto = new YtmsUserDto();
        mockUserDto.setEmailAdd(email);
        mockUserDto.setFullName("Dnyanesh Somwanshi");
        mockUserDto.setAccountStatus(UserAccountStatusTypes.APPROVED);

        when(userService.getUserByEmailAdd(anyString())).thenReturn(mockUserDto);
    }

    @When("the user attempts to log in")
    public void the_user_attempts_to_log_in() {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                authResponse = new JwtAuthResponse();
                authResponse.setToken(tokenHelper.generateToken(userDetails));
                authResponse.setStatus(RequestStatusTypes.SUCCESS.toString());
            }
        } catch (BadCredentialsException e) {
            authResponse = new JwtAuthResponse();
            authResponse.setStatus(RequestStatusTypes.FAILED.toString());
        }
    }

    @Then("the login should be successful")
    public void the_login_should_be_successful() {
        assertNotNull("Auth response should not be null", authResponse);
        assertEquals("Login should be successful", RequestStatusTypes.SUCCESS.toString(), authResponse.getStatus());
    }

    @Then("a JWT token should be generated")
    public void a_jwt_token_should_be_generated() {
        assertNotNull("JWT token should be generated", authResponse.getToken());
        assertEquals("JWT token should match the expected token", generatedToken, authResponse.getToken());
    }

    @Then("the response status should be {string}")
    public void the_response_status_should_be(String expectedStatus) {
        assertNotNull("Response status should not be null", authResponse.getStatus());
        assertEquals("Response status should match", expectedStatus, authResponse.getStatus().toString());
    }
}