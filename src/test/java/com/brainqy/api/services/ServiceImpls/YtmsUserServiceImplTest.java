package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */

import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.ProfileCompletionDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.security.userdetails.CustomUserDetails;
import com.brainqy.api.services.IServices.IOrganizationService;
import com.brainqy.api.services.IServices.IReferralService;
import com.brainqy.api.services.IServices.IUserRoleService;
import com.brainqy.api.util.EmailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class YtmsUserServiceImplTest {

    @Mock
    private YtmsUserRepository userRepository;

    @Mock
    private IOrganizationService organizationService;

    @Mock
    private IUserRoleService userRoleService;

    @Mock
    private IReferralService referralService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailUtil emailUtil;

    @InjectMocks
    private YtmsUserServiceImpl userService;

    private YtmsUserDto userDto;
    private YtmsUser user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = new YtmsUserDto();
        userDto.setEmailAdd("test@example.com");
        userDto.setPassword("password");
        userDto.setConfirmPassword("password");
        userDto.setFullName("Test User");
        user = new YtmsUser();
        user.setFullName("Test User");
        user.setEmailAdd("test@example.com");

        // Mock SecurityContext and Authentication
        CustomUserDetails auth = mock(CustomUserDetails.class);
        when(auth.getEmailAdd()).thenReturn("test@example.com");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(auth);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

@Test
void changePassword_ShouldReturnFailedWhenUserNotFound() {
    Map<String, String> map = new HashMap<>();
    map.put("password", "newPassword");
    map.put("oldPassword", "oldPassword");

    when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.empty());

    ResponseWrapperDto result = userService.changePassword(map);

    assertNotNull(result);
    assertEquals("FAILED", result.getStatus());
}
    @Test
    void getAllPendingUsers_ShouldReturnPendingUsers() {
        List<YtmsUser> pendingUsers = Collections.singletonList(user);
        when(userRepository.getAllPendingUsers()).thenReturn(pendingUsers);
        when(modelMapper.map(any(YtmsUser.class), eq(YtmsUserDto.class))).thenReturn(userDto);

        List<YtmsUserDto> result = userService.getAllPendingUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmailAdd());
    }

    @Test
    void approvePendingUser_ShouldApproveUser() {
        when(userRepository.approvePendingUser(anyString())).thenReturn(1);

        Boolean result = userService.approvePendingUser("test@example.com");

        assertTrue(result);
    }

    @Test
    void approvePendingUser_ShouldThrowExceptionWhenEmailIsEmpty() {
        assertThrows(ApplicationException.class, () -> userService.approvePendingUser(""));
    }

    @Test
    void approvePendingUser_ShouldThrowExceptionWhenApprovalFails() {
        when(userRepository.approvePendingUser(anyString())).thenReturn(0);

        assertThrows(ApplicationException.class, () -> userService.approvePendingUser("test@example.com"));
    }

    @Test
    void declinePendingUser_ShouldDeclineUser() {
        when(userRepository.declinePendingUser(anyString())).thenReturn(1);

        Boolean result = userService.declinePendingUser("test@example.com");

        assertTrue(result);
    }

    @Test
    void declinePendingUser_ShouldThrowExceptionWhenEmailIsEmpty() {
        assertThrows(ApplicationException.class, () -> userService.declinePendingUser(""));
    }

    @Test
    void declinePendingUser_ShouldThrowExceptionWhenDeclineFails() {
        when(userRepository.declinePendingUser(anyString())).thenReturn(0);

        assertThrows(ApplicationException.class, () -> userService.declinePendingUser("test@example.com"));
    }

    @Test
    void forgotPassword_ShouldSendResetEmail() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        ResponseWrapperDto result = userService.forgotPassword("dvsomwanshi@gmail.com");

        assertNotNull(result);
        assertEquals("please check your email to reset password", result.getMessage());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void forgotPassword_ShouldReturnFailedWhenUserNotFound() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseWrapperDto result = userService.forgotPassword("dvsomwanshi@gmail.com");

        assertNotNull(result);
        assertEquals("please check your email to reset password", result.getMessage());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void forgotPassword_ShouldReturnFailedWhenEmailIsEmpty() {
        ResponseWrapperDto result = userService.forgotPassword("");

        assertNotNull(result);
        assertEquals("Email is empty !", result.getMessage());
        assertEquals("FAILED", result.getStatus());
    }

    @Test
    void resetPassword_ShouldResetPassword() {
        Map<String, String> map = new HashMap<>();
        map.put("email", Base64.getEncoder().encodeToString("test@example.com".getBytes()));
        map.put("password", "newPassword");

        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Boolean result = userService.resetPassword(map);

        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetPassword_ShouldReturnFalseWhenUserNotFound() {
        Map<String, String> map = new HashMap<>();
        map.put("email", Base64.getEncoder().encodeToString("dvsomwanshi@gmail.com".getBytes()));
        map.put("password", "newPassword");

        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.empty());

        Boolean result = userService.resetPassword(map);

        assertFalse(result);
    }

    @Test
    void changePassword_ShouldChangePassword() {
        Map<String, String> map = new HashMap<>();
        map.put("password", "newPassword");
        map.put("oldPassword", "oldPassword");

        CustomUserDetails auth = mock(CustomUserDetails.class);
        when(auth.getEmailAdd()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        ResponseWrapperDto result = userService.changePassword(map);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllTrainers_ShouldReturnAllTrainers() {
        List<YtmsUser> trainers = Collections.singletonList(user);
        when(userRepository.findAllTrainers()).thenReturn(trainers);
        when(modelMapper.map(any(YtmsUser.class), eq(YtmsUserDto.class))).thenReturn(userDto);

        ResponseWrapperDto result = userService.getAllTrainers();

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void SetLoginHistory_ShouldSetLoginHistory() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        String result = userService.SetLoginHistory("test@example.com");

        assertEquals("LoginHistory Success", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void SetLoginHistory_ShouldReturnFailedWhenUserNotFound() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.empty());

        String result = userService.SetLoginHistory("test@example.com");

        assertEquals("LoginHistory Failed!", result);
    }

    @Test
    void getProfileCompletion_ShouldReturnProfileCompletion() {
        ProfileCompletionDto result = userService.getProfileCompletion(userDto);

        assertNotNull(result);
        assertTrue(result.getProfileCompleted() >= 0);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<YtmsUser> users = Collections.singletonList(user);
        when(userRepository.getAllUsers()).thenReturn(users);
        when(modelMapper.map(any(YtmsUser.class), eq(YtmsUserDto.class))).thenReturn(userDto);

        List<YtmsUserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmailAdd());
    }
}