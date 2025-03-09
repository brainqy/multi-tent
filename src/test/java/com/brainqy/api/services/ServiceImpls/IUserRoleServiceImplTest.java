package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import com.brainqy.api.domain.UserRole;
import com.brainqy.api.dto.UserRoleDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.UserRoleRepository;
import com.brainqy.api.services.ServiceImpls.IUserRoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IUserRoleServiceImplTest {

    @Mock
    private UserRoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IUserRoleServiceImpl userRoleService;

    private UserRoleDto userRoleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRoleDto = new UserRoleDto(1L, "ROLE_USER");
    }

    @Test
    void createUserRole_ShouldCreateRole() {
        when(modelMapper.map(any(UserRoleDto.class), eq(UserRole.class))).thenReturn(new UserRole());
        when(roleRepository.save(any(UserRole.class))).thenReturn(new UserRole());
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDto.class))).thenReturn(userRoleDto);

        UserRoleDto result = userRoleService.createUserRole(userRoleDto);

        assertNotNull(result);
        assertEquals(userRoleDto.getRoleId(), result.getRoleId());
        verify(roleRepository, times(1)).save(any(UserRole.class));
    }

    @Test
    void createUserRole_ShouldThrowExceptionWhenDtoIsNull() {
        assertThrows(ApplicationException.class, () -> userRoleService.createUserRole(null));
        verify(roleRepository, never()).save(any(UserRole.class));
    }

    @Test
    void getUserRoleById_ShouldReturnRole() {
        when(roleRepository.getUserRoleByRoleId(anyLong())).thenReturn(new UserRole());
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDto.class))).thenReturn(userRoleDto);

        UserRoleDto result = userRoleService.getUserRoleById(1L);

        assertNotNull(result);
        assertEquals(userRoleDto.getRoleId(), result.getRoleId());
    }

    @Test
    void getUserRoleById_ShouldThrowExceptionWhenRoleNotFound() {
        when(roleRepository.getUserRoleByRoleId(anyLong())).thenReturn(null);

        assertThrows(ApplicationException.class, () -> userRoleService.getUserRoleById(1L));
    }

    @Test
    void getUserRoleByRoleName_ShouldReturnRole() {
        when(roleRepository.getUserRoleByRoleName(anyString())).thenReturn(new UserRole());
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDto.class))).thenReturn(userRoleDto);

        UserRoleDto result = userRoleService.getUserRoleByRoleName("ROLE_USER");

        assertNotNull(result);
        assertEquals(userRoleDto.getRoleTypes(), result.getRoleTypes());
    }

    @Test
    void getUserRoleByRoleName_ShouldThrowExceptionWhenRoleNotFound() {
        when(roleRepository.getUserRoleByRoleName(anyString())).thenReturn(null);

        assertThrows(ApplicationException.class, () -> userRoleService.getUserRoleByRoleName("ROLE_USER"));
    }

    @Test
    void getAllUserRoles_ShouldReturnRoles() {
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole());
        when(roleRepository.getAllUserRoles()).thenReturn(userRoles);
        when(modelMapper.map(any(UserRole.class), eq(UserRoleDto.class))).thenReturn(userRoleDto);

        Set<UserRoleDto> result = userRoleService.getAllUserRoles();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllUserRoles_ShouldReturnEmptySet() {
        when(roleRepository.getAllUserRoles()).thenReturn(new HashSet<>());

        Set<UserRoleDto> result = userRoleService.getAllUserRoles();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void roleUpdateScheduler_ShouldCreateDefaultRolesWhenNoRolesExist() {
        when(roleRepository.getAllUserRoles()).thenReturn(new HashSet<>());
        when(modelMapper.map(any(UserRoleDto.class), eq(UserRole.class))).thenReturn(new UserRole());

        userRoleService.roleUpdateScheduler();

        verify(roleRepository, times(3)).save(any(UserRole.class));
    }

    @Test
    void roleUpdateScheduler_ShouldNotCreateDefaultRolesWhenRolesExist() {
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole());
        when(roleRepository.getAllUserRoles()).thenReturn(userRoles);

        userRoleService.roleUpdateScheduler();

        verify(roleRepository, never()).save(any(UserRole.class));
    }
}
