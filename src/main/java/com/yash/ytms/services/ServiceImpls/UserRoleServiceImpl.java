package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.constants.UserRoleTypes;
import com.yash.ytms.domain.UserRole;
import com.yash.ytms.dto.UserRoleDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.UserRoleRepository;
import com.yash.ytms.services.IServices.IUserRoleService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRoleRepository roleRepository;

    @Override
    public UserRoleDto createUserRole(UserRoleDto userRoleDto) {
        UserRole userRole = null;

        if (ObjectUtils.isNotEmpty(userRoleDto)) {
            userRole = this
                    .modelMapper
                    .map(userRoleDto, UserRole.class);

            userRole = this
                    .roleRepository
                    .save(userRole);

            userRoleDto = this
                    .modelMapper
                    .map(userRole, UserRoleDto.class);
        } else {
            throw new ApplicationException("User Role Details are empty or null");
        }

        return userRoleDto;
    }

    @Override
    public UserRoleDto getUserRoleById(Long roleId) {
        UserRoleDto userRoleDto = null;
        UserRole userRole = null;

        if (ObjectUtils.isNotEmpty(roleId)) {
            userRole = this
                    .roleRepository
                    .getUserRoleByRoleId(roleId);

            if (ObjectUtils.isNotEmpty(userRole)) {
                userRoleDto = this
                        .modelMapper
                        .map(userRole, UserRoleDto.class);
            } else {
                throw new ApplicationException("User Role not found with the provided Id");
            }
        } else {
            throw new ApplicationException("User Role Id is empty or null");
        }

        return userRoleDto;
    }

    @Override
    public UserRoleDto getUserRoleByRoleName(String roleName) {
        UserRoleDto userRoleDto = null;
        UserRole userRole = null;

        if (StringUtils.isNotEmpty(roleName)) {
            userRole = this
                    .roleRepository
                    .getUserRoleByRoleName(roleName);

            if (ObjectUtils.isNotEmpty(userRole)) {
                userRoleDto = this
                        .modelMapper
                        .map(userRole, UserRoleDto.class);
            } else {
                throw new ApplicationException("User Role not found with the provided role name");
            }
        } else {
            throw new ApplicationException("User Role name is empty or null");
        }

        return userRoleDto;
    }

    @Override
    public Set<UserRoleDto> getAllUserRoles() {
        Set<UserRoleDto> userRolesDto = new HashSet<>();
        Set<UserRole> userRoles = new HashSet<>();

        userRoles = this
                .roleRepository
                .getAllUserRoles();

        if (!userRoles.isEmpty()) {
            userRolesDto = userRoles
                    .stream()
                    .map(ur -> this
                            .modelMapper
                            .map(ur, UserRoleDto.class))
                    .collect(Collectors.toSet());
        }
        return userRolesDto;
    }

    @Override
    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = 1800000)
    public void roleUpdateScheduler() {
        Set<UserRoleDto> allUserRoles = new HashSet<>();
        allUserRoles = this
                .getAllUserRoles();

        if (allUserRoles.isEmpty()) {

            UserRoleDto requesterUserRole = new UserRoleDto(501L, UserRoleTypes.ROLE_REQUESTER.toString());
            UserRoleDto trainerUserRole = new UserRoleDto(502L, UserRoleTypes.ROLE_TRAINER.toString());
            UserRoleDto techManagerUserRole = new UserRoleDto(503L, UserRoleTypes.ROLE_TECHNICAL_MANAGER.toString());

            allUserRoles.add(requesterUserRole);
            allUserRoles.add(trainerUserRole);
            allUserRoles.add(techManagerUserRole);

            allUserRoles.forEach(this :: createUserRole);
        }
    }
}
