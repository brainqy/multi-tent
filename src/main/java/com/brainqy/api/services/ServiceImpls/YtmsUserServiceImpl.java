package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.Organization;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.constants.RequestStatusTypes;
import com.brainqy.api.constants.UserAccountStatusTypes;
import com.brainqy.api.constants.UserRoleTypes;
import com.brainqy.api.domain.LoginHistory;
import com.brainqy.api.dto.*;
import com.brainqy.api.dto.*;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.security.userdetails.CustomUserDetails;
import com.brainqy.api.services.IServices.IOrganizationService;
import com.brainqy.api.services.IServices.IReferralService;
import com.brainqy.api.services.IServices.IUserRoleService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import com.brainqy.api.util.EmailUtil;
import com.brainqy.api.util.OrganizationConverter;
import com.brainqy.api.util.YtmsUserConverter;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
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
public class YtmsUserServiceImpl implements IYtmsUserService {
    private static final Logger lOGGER = LoggerFactory.getLogger(YtmsUserServiceImpl.class);

    @Autowired
    private YtmsUserRepository userRepository;
    @Autowired
    private IReferralService referralService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private YtmsUserConverter ytmsUserConverter;
    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrganizationConverter organizationConverter;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    @Transactional
    public YtmsUserDto createNewUser(YtmsUserDto userDto) {
        lOGGER.info("Creating new user with email: {}", userDto != null ? userDto.getEmailAdd() : "null");

        if (ObjectUtils.isEmpty(userDto)) {
            lOGGER.error("Invalid user details: userDto is empty or null");
            throw new ApplicationException("Invalid user details");
        }

        lOGGER.info("Checking if user already exists with email: {}", userDto.getEmailAdd());
        Optional<YtmsUser> user = userRepository.getUserByEmail(userDto.getEmailAdd());

        if (user.isPresent()) {
            lOGGER.warn("User already exists with this email address: {}", userDto.getEmailAdd());
            throw new ApplicationException("User already exists with this email address");
        }

        if (!StringUtils.equals(userDto.getPassword(), userDto.getConfirmPassword())) {
            lOGGER.error("Password mismatch for user: {}", userDto.getEmailAdd());
            throw new ApplicationException("Password did not match, please try again");
        }

        lOGGER.info("Passwords match. Proceeding with user creation.");

        if (userDto.getRef() != null) {
            String newEmail = new String(Base64.getDecoder().decode(userDto.getRef()));
            userDto.setRef(newEmail);
            lOGGER.info("Decoded referral email: {}", newEmail);
        }

        UserRoleDto userRoleDto = userRoleService.getUserRoleByRoleName(UserRoleTypes.ROLE_REQUESTER.toString());
        userDto.setUserRole(userRoleDto);
        lOGGER.info("Assigned role: {}", userRoleDto.getRoleId());

        referralService.setReferralEntity(userDto);
        lOGGER.info("Referral entity set for user: {}", userDto.getEmailAdd());

        user = Optional.ofNullable(modelMapper.map(userDto, YtmsUser.class));
        user.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.get().setAccountStatus(UserAccountStatusTypes.PENDING);
        lOGGER.info("User password encrypted and account status set to PENDING");

        // Set default organization if not provided
        Organization organization = user.get().getOrganization();
        if (organization == null) {
            OrganizationDto defaultOrg = this.organizationService.findOrganizationByOrgCode("BRAINQY");
            if (defaultOrg == null) {
                lOGGER.error("Default organization not found");
                throw new ApplicationException("Default organization not found");
            }
            lOGGER.info("Fetched default organization: {}", defaultOrg.getOrgName());
            organization = OrganizationConverter.convertToEntity(defaultOrg);
            user.get().setOrganization(organization);
        }

        lOGGER.info("Associated user with organization: {}", organization.getOrgName());

        user = Optional.of(userRepository.save(user.get()));
        lOGGER.info("User successfully created with email: {}", userDto.getEmailAdd());
       return  ytmsUserConverter.convertToDto(user.get());
       // return modelMapper.map(user, YtmsUserDto.class);
    }
    @Override
    @Transactional
    public YtmsUserDto getUserByEmailAdd(String emailAdd) {
        lOGGER.info("Fetching user by email: {}", emailAdd);

        if (StringUtils.isEmpty(emailAdd)) {
            lOGGER.error("Email address is empty");
            throw new ApplicationException("Email address is empty");
        }

        Optional<YtmsUser> user = this.userRepository.getUserByEmail(emailAdd);

        if (user.isPresent()) {
            lOGGER.info("User found for email: {}", emailAdd);
            YtmsUserDto userDto = this.modelMapper.map(user.get(), YtmsUserDto.class);
            lOGGER.info("Successfully mapped user entity to DTO for email: {}", emailAdd);
            return userDto;
        } else {
            lOGGER.warn("No user found for email: {}", emailAdd);
            throw new ApplicationException("User not found with email: " + emailAdd);
        }
    }


    @Override
    @Transactional
    public List<YtmsUserDto> getAllPendingUsers() {
        List<YtmsUser> pendingUsers = this.userRepository.getAllPendingUsers();
        return pendingUsers.stream().map(penUser -> this.modelMapper.map(penUser, YtmsUserDto.class)).toList();
    }

    @Override
    @Transactional
    public Boolean approvePendingUser(String emailAdd) {
        lOGGER.info("Approving pending user with email: {}", emailAdd);

        if (StringUtils.isEmpty(emailAdd)) {
            lOGGER.error("Email address is empty.");
            throw new ApplicationException("Email address is empty.");
        }

        Integer status = this.userRepository.approvePendingUser(emailAdd);

        if (status == 1) {
            lOGGER.info("User successfully approved: {}", emailAdd);
            return true;
        } else {
            lOGGER.warn("User approval failed for email: {}", emailAdd);
            throw new ApplicationException("User not approved");
        }
    }

    @Override
    @Transactional
    public Boolean declinePendingUser(String emailAdd) {
        lOGGER.info("Approving pending user with email: {}", emailAdd);
        if (StringUtils.isNotEmpty(emailAdd)) {
            Integer status = this.userRepository.declinePendingUser(emailAdd);
            if (status == 1)
                return true;
            else
                lOGGER.info("Failed while declining user account");
                throw new ApplicationException("Failed while declining user account");
        } else {
            lOGGER.info("Email address is empty.");
            throw new ApplicationException("Email address is empty.");
        }
    }

    @Override
    @Transactional
    public ResponseWrapperDto forgotPassword(String email) {
        lOGGER.info("Forgot password for :{}",email);
        ResponseWrapperDto responseWrapperDto = new ResponseWrapperDto();
        if (StringUtils.isNotEmpty(email)) {
            try {
                Optional<YtmsUser> ytmsUser = this.userRepository.getUserByEmail(email);
                if (ObjectUtils.isNotEmpty(ytmsUser)) {
                    emailUtil.sendSetPasswordEmail(email);
                    responseWrapperDto.setMessage("please check your email to reset password");
                    responseWrapperDto.setStatus(RequestStatusTypes.SUCCESS.toString());
                } else {
                    responseWrapperDto.setMessage("User does not exist with the provided email !");
                    responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
                }

            } catch (MessagingException e) {
                responseWrapperDto.setMessage("unable to set password !");
                responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
            }

        } else {
            responseWrapperDto.setMessage("Email is empty !");
            responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());

        }
        return responseWrapperDto;
    }

    @Override
    @Transactional
    public Boolean resetPassword(Map<String, String> map) {
        lOGGER.info("Resetting password");
        String email = map.get("email");
        String password = map.get("password");
        String newEmail = new String(Base64.getDecoder().decode(email));
        Optional<YtmsUser> user = this.userRepository.getUserByEmail(newEmail);
        if (user != null && StringUtils.isNotEmpty(password)) {
            user.get().setPassword(this.passwordEncoder.encode(password));
            System.out.println(" changing password for " + user.toString());
            this.userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseWrapperDto changePassword(Map<String, String> map) {
        lOGGER.info("Changing password");
        String password = map.get("password");
        String oldPassword = map.get("oldPassword");
        ResponseWrapperDto responseWrapperDto = new ResponseWrapperDto();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        CustomUserDetails auth = (CustomUserDetails) securityContext.getAuthentication().getPrincipal();
        String email = auth.getEmailAdd();

        Optional<YtmsUser> user = this.userRepository.getUserByEmail(email);

        if (user != null && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(oldPassword)) {
            if (passwordEncoder.matches(oldPassword, user.get().getPassword())) {
                user.get().setPassword(passwordEncoder.encode(password));
                this.userRepository.save(user.get());
                responseWrapperDto.setStatus(RequestStatusTypes.SUCCESS.toString());
                lOGGER.info("Password changed successfully.");
            } else {
                lOGGER.info("Old password doesn't match.");
            }
        } else {
            responseWrapperDto.setMessage("User not found or password is empty.");
            responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
            lOGGER.info("User not found or password is empty.");
        }

        return responseWrapperDto;
    }

    @Override
    @Transactional
    public ResponseWrapperDto getAllTrainers() {
        lOGGER.info("Fetching all trainers.");
        List<YtmsUser> allTrainers = this.userRepository.findAllTrainers();
        List<YtmsUserDto> trainersDtoList = allTrainers != null && !allTrainers.isEmpty()
                ? allTrainers.stream()
                .map(yur -> this.modelMapper.map(yur, YtmsUserDto.class))
                .collect(Collectors.toList())
                : Collections.emptyList();

        ResponseWrapperDto wrapperDto = new ResponseWrapperDto();
        wrapperDto.setData(trainersDtoList);
        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setMessage(trainersDtoList.isEmpty() ? "No trainers found" : "Trainers fetched successfully");

        return wrapperDto;
    }




    @Override
    @Transactional
    public String SetLoginHistory(String currentUserEmail) {
        lOGGER.info("Setting login history for user email: {}", currentUserEmail);

        Optional<YtmsUser> currentUser = userRepository.getUserByEmail(currentUserEmail);

        if (currentUser.isPresent()) {
            lOGGER.info("User found with email: {}", currentUserEmail);

            // Create new login history
            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setLoginTime(LocalDateTime.now());
            loginHistory.setUserHistory(currentUser.get());

            // Add login history to user
            currentUser.get().addLoginHistory(loginHistory);

            // Save updated user entity
            userRepository.save(currentUser.get());

            lOGGER.info("Login history saved successfully for user: {}", currentUserEmail);
            return "LoginHistory Success";
        } else {
            lOGGER.warn("User not found with email: {}", currentUserEmail);
        }

        lOGGER.error("Login history update failed for user: {}", currentUserEmail);
        return "LoginHistory Failed!";
    }

    @Override
    @Transactional
    public ProfileCompletionDto getProfileCompletion(YtmsUserDto user) {
        lOGGER.info("Getting profile completion details for user: {}", user.getEmailAdd()); // Assuming getId() exists

        ProfileCompletionDto profileDto = new ProfileCompletionDto();
        int totalFields = 0;
        int completedFields = 0;
        ArrayList<Field> uncompletedFields = new ArrayList<>();

        Field[] fields = user.getClass().getDeclaredFields();
        lOGGER.info("Total fields in YtmsUserDto: {}", fields.length);

        for (Field field : fields) {
            totalFields++;
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(user);
                if (fieldValue != null && !fieldValue.toString().isEmpty()) {
                    completedFields++;
                    lOGGER.debug("Field '{}' is completed.", field.getName());
                } else {
                    uncompletedFields.add(field);
                    lOGGER.debug("Field '{}' is uncompleted.", field.getName());
                }
            } catch (IllegalAccessException e) {
                lOGGER.error("Error accessing field '{}': {}", field.getName(), e.getMessage());
            }
        }

        int completionPercentage = (int) Math.round((double) completedFields / totalFields * 100);
        profileDto.setProfileCompleted(completionPercentage);
        profileDto.setUncompletedFields(uncompletedFields);

        lOGGER.info("Profile completion calculated: {}%", completionPercentage);
        lOGGER.info("Total completed fields: {}, Total uncompleted fields: {}", completedFields, uncompletedFields.size());

        return profileDto;
    }

    @Override
    @Transactional
    public List<YtmsUserDto> getAllUsers() {
        lOGGER.info("Fetching all users.");
        List<YtmsUser> users = userRepository.getAllUsers();
        lOGGER.info("Total users fetched from repository: {}", users.size());

        List<YtmsUserDto> usrdto = users.stream()
                .map(usr -> {
                    lOGGER.debug("Mapping user: {}", usr.getEmailAdd()); // Assuming `getId()` exists
                    return modelMapper.map(usr, YtmsUserDto.class);
                })
                .collect(Collectors.toList());
        lOGGER.info("Successfully mapped {} users to DTOs.", usrdto.size());
        return usrdto;
    }

}
