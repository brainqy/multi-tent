package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.UserRoleDto;

import java.util.Set;

/**
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
public interface IUserRoleService {

    UserRoleDto createUserRole(UserRoleDto userRoleDto);

    UserRoleDto getUserRoleById(Long roleId);

    UserRoleDto getUserRoleByRoleName(String roleName);

    Set<UserRoleDto> getAllUserRoles();

    void roleUpdateScheduler();
}
