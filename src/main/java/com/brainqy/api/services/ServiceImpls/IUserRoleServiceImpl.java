package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.constants.UserRoleTypes;
import com.brainqy.api.dto.UserRoleDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.UserRoleRepository;
import com.brainqy.api.services.IServices.IUserRoleService;
import com.brainqy.api.domain.UserRole;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
@Service
public class IUserRoleServiceImpl implements IUserRoleService {
    private static final Logger lOGGER = LoggerFactory.getLogger(IUserRoleServiceImpl.class);

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
            lOGGER.info("User Role Details are empty or null");
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
                lOGGER.info("User Role not found with the provided Id");
                throw new ApplicationException("User Role not found with the provided Id");
            }
        } else {
            lOGGER.info("User Role Id is empty or null");
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
                lOGGER.info("User Role not found with the provided role name");
                throw new ApplicationException("User Role not found with the provided role name");
            }
        } else {
            lOGGER.info("User Role name is empty or null");
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
