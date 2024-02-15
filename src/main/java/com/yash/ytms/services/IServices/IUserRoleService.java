package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.UserRoleDto;

import java.util.Set;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
public interface IUserRoleService {

    UserRoleDto createUserRole(UserRoleDto userRoleDto);

    UserRoleDto getUserRoleById(Long roleId);

    UserRoleDto getUserRoleByRoleName(String roleName);

    Set<UserRoleDto> getAllUserRoles();

    void roleUpdateScheduler();
}
