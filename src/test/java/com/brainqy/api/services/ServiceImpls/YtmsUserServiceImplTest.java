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
import com.brainqy.api.dto.OrganizationDto;
import com.brainqy.api.dto.UserRoleDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.IServices.IOrganizationService;
import com.brainqy.api.services.IServices.IReferralService;
import com.brainqy.api.services.IServices.IUserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

    @InjectMocks
    private YtmsUserServiceImpl userService;

    private YtmsUserDto userDto;
    private YtmsUser user;
    private OrganizationDto organizationDto;
    private UserRoleDto userRoleDto;

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
        organizationDto = new OrganizationDto();
        organizationDto.setOrgCode("BRAINQY");
        organizationDto.setOrgName("BrainQy");
        userRoleDto = new UserRoleDto();
        userRoleDto.setRoleId(1L);
        userRoleDto.setRoleTypes("ROLE_REQUESTER");
    }

    @Test
    void getUserByEmailAdd_ShouldReturnUser() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(YtmsUser.class), eq(YtmsUserDto.class))).thenReturn(userDto);

        YtmsUserDto result = userService.getUserByEmailAdd("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmailAdd());
    }

    @Test
    void getUserByEmailAdd_ShouldThrowExceptionWhenEmailIsEmpty() {
        assertThrows(ApplicationException.class, () -> userService.getUserByEmailAdd(""));
    }

    @Test
    void getUserByEmailAdd_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> userService.getUserByEmailAdd("test@example.com"));
    }

    @Test
    void createNewUser_ShouldCreateUserWithDefaultOrganization() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(any(YtmsUserDto.class), eq(YtmsUser.class))).thenReturn(user);
        when(userRepository.save(any(YtmsUser.class))).thenReturn(user);
        when(modelMapper.map(any(YtmsUser.class), eq(YtmsUserDto.class))).thenReturn(userDto);
        when(organizationService.findOrganizationByOrgCode(anyString())).thenReturn(organizationDto);
        when(userRoleService.getUserRoleByRoleName(anyString())).thenReturn(userRoleDto);

        YtmsUserDto result = userService.createNewUser(userDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmailAdd());
        assertNotNull(user.getOrganization());
        assertEquals("BrainQy", user.getOrganization().getOrgName());
    }

    @Test
    void createNewUser_ShouldThrowExceptionWhenUserAlreadyExists() {
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(ApplicationException.class, () -> userService.createNewUser(userDto));
    }

    @Test
    void createNewUser_ShouldThrowExceptionWhenPasswordsDoNotMatch() {
        userDto.setPassword("password");
        userDto.setConfirmPassword("differentPassword");

        assertThrows(ApplicationException.class, () -> userService.createNewUser(userDto));
    }

    @Test
    void createNewUser_ShouldThrowExceptionWhenUserDtoIsEmpty() {
        assertThrows(ApplicationException.class, () -> userService.createNewUser(null));
    }
}