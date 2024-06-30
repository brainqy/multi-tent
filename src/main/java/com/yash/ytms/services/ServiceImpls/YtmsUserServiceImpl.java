package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.constants.RequestStatusTypes;
import com.yash.ytms.constants.UserAccountStatusTypes;
import com.yash.ytms.constants.UserRoleTypes;
import com.yash.ytms.domain.LoginHistory;
import com.yash.ytms.domain.Organization;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.*;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.security.userdetails.CustomUserDetails;
import com.yash.ytms.services.IServices.IOrganizationService;
import com.yash.ytms.services.IServices.IReferralService;
import com.yash.ytms.services.IServices.IUserRoleService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import com.yash.ytms.util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
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
public class YtmsUserServiceImpl implements IYtmsUserService {

    @Autowired
    private YtmsUserRepository userRepository;
    @Autowired
    private IReferralService referralService;
    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    @Transactional
    public YtmsUserDto createNewUser(YtmsUserDto userDto) {
        OrganizationDto defaultOrg =this.organizationService.findOrganizationByOrgCode("BRAINQY");
        // Save the organization first
        Organization organization = modelMapper.map(defaultOrg,Organization.class);

        Optional<YtmsUser> user = null;
        if (ObjectUtils.isNotEmpty(userDto)) {
            user = userRepository.getUserByEmail(userDto.getEmailAdd());
            if (ObjectUtils.isEmpty(user)) {
                if (StringUtils.equals(userDto.getPassword(), userDto.getConfirmPassword())) {
                    if(userDto.getRef()!=null){
                        String newEmail = new String(Base64.getDecoder().decode(userDto.getRef()));
                        userDto.setRef(newEmail);
                    }

                    UserRoleDto userRoleDto = userRoleService.getUserRoleByRoleName(UserRoleTypes.ROLE_REQUESTER.toString());
                    userDto.setUserRole(userRoleDto);
                    referralService.setReferralEntity(userDto);

                    user = Optional.ofNullable(modelMapper.map(userDto, YtmsUser.class));
                    user.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
                    user.get().setAccountStatus(UserAccountStatusTypes.PENDING);

                    // Associate the saved organization with the user
                    user.get().setOrganization(organization);

                    user = Optional.of(userRepository.save(user.get()));
                    userDto = modelMapper.map(user, YtmsUserDto.class);
                } else {
                    throw new ApplicationException("Password did not matched, please try again");
                }
            } else {
                throw new ApplicationException("User already exists with this email address");
            }
        } else {
            throw new ApplicationException("Invalid user details");
        }
        return userDto;
    }


    @Override
    public YtmsUserDto getUserByEmailAdd(String emailAdd) {
        YtmsUserDto userDto = null;
        Optional<YtmsUser> user = null;

        if (StringUtils.isNotEmpty(emailAdd)) {
            user = this.userRepository.getUserByEmail(emailAdd);

            userDto = this
                    .modelMapper
                    .map(user, YtmsUserDto.class);
        } else {
            throw new ApplicationException("Email add is empty");
        }
        return userDto;
    }

    @Override
    public List<YtmsUserDto> getAllPendingUsers() {
        List<YtmsUser> pendingUsers = this.userRepository.getAllPendingUsers();
        return pendingUsers.stream().map(penUser -> this.modelMapper.map(penUser, YtmsUserDto.class)).toList();
    }

    @Override
    public Boolean approvePendingUser(String emailAdd) {
        if (StringUtils.isNotEmpty(emailAdd)) {
            Integer status = this.userRepository.approvePendingUser(emailAdd);
            if (status == 1)
                return true;
            else
                throw new ApplicationException("User not approved");
        } else {
            throw new ApplicationException("Email address is empty.");
        }
    }

    @Override
    public Boolean declinePendingUser(String emailAdd) {
        if (StringUtils.isNotEmpty(emailAdd)) {
            Integer status = this.userRepository.declinePendingUser(emailAdd);
            if (status == 1)
                return true;
            else
                throw new ApplicationException("Failed while declining user account");
        } else {
            throw new ApplicationException("Email address is empty.");
        }
    }

    @Override
    public ResponseWrapperDto forgotPassword(String email) {
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
    public Boolean resetPassword(Map<String, String> map) {
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
    public ResponseWrapperDto changePassword(Map<String, String> map) {
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
                System.out.println("Password changed successfully.");
            } else {
                System.out.println("Old password doesn't match.");
            }
        } else {
            responseWrapperDto.setMessage("User not found or password is empty.");
            responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
            System.out.println("User not found or password is empty.");
        }

        return responseWrapperDto;
    }

    @Override
    public List<YtmsUserDto> getAllTrainers() {
        Optional<List<YtmsUser>> optionalAllTrainers = Optional.ofNullable(this.userRepository.findAllTrainers());

        return optionalAllTrainers
                .filter(allTrainers -> !allTrainers.isEmpty())
                .map(allTrainers -> allTrainers.stream()
                        .map(yur -> this.modelMapper.map(yur, YtmsUserDto.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ApplicationException("No Trainers found!"));
    }


    @Override
    public String SetLoginHistory(String currentUserEmail) {
        Optional<YtmsUser> currentUser = userRepository.getUserByEmail(currentUserEmail);
        // create new login history
        if (currentUser != null) {
            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setLoginTime(LocalDateTime.now().minusDays(0));
            loginHistory.setUserHistory(currentUser.get());
            //currentUser.setLastLogin(Instant.now());
            currentUser.get().addLoginHistory(loginHistory);
            userRepository.save(currentUser.get());
            return  "LoginHistory Success ";
        }

 return "LoginHistory Failed !";
    }

    @Override
    public ProfileCompletionDto getProfileCompletion(YtmsUserDto user) {
        ProfileCompletionDto profileDto= new ProfileCompletionDto();
        int totalFields = 0;
        int completedFields = 0;
        ArrayList<Field>uncompletedFields= new ArrayList<>();
        Field[] fields = user.getClass().getDeclaredFields();
        for (Field field : fields) {
            totalFields++;
            field.setAccessible(true);
            try {
                if (field.get(user) != null && !field.get(user).toString().isEmpty()) {
                    completedFields++;
                }else
                {
                    uncompletedFields.add(field);

                }
            } catch (IllegalAccessException e) {
                // handle exception
            }
        }
        profileDto.setProfileCompleted((int) Math.round((double) completedFields / totalFields * 100));
        profileDto.setUncompletedFields(uncompletedFields);
        return profileDto;
    }
}
