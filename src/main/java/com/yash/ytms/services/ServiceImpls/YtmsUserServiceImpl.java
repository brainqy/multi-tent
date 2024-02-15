package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.constants.RequestStatusTypes;
import com.yash.ytms.constants.UserAccountStatusTypes;
import com.yash.ytms.constants.UserRoleTypes;
import com.yash.ytms.domain.LoginHistory;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.ProfileCompletionDto;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.UserRoleDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.security.userdetails.CustomUserDetails;
import com.yash.ytms.services.IServices.IReferralService;
import com.yash.ytms.services.IServices.IUserRoleService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import com.yash.ytms.util.EmailUtil;
import jakarta.mail.MessagingException;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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
    private IUserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public YtmsUserDto createNewUser(YtmsUserDto userDto) {
        YtmsUser user = null;
        if (ObjectUtils.isNotEmpty(userDto)) {

            user = this
                    .userRepository
                    .getUserByEmail(userDto.getEmailAdd());

            if (ObjectUtils.isEmpty(user)) {
                //user do not exist, new user will be created
                if (StringUtils.equals(userDto.getPassword(), userDto.getConfirmPassword())) {

                    UserRoleDto userRoleDto = this
                            .userRoleService
                            .getUserRoleByRoleName(UserRoleTypes.ROLE_REQUESTER.toString());

                    userDto.setUserRole(userRoleDto);
                    this.referralService.setReferralEntity(userDto);

                    user = this
                            .modelMapper
                            .map(userDto, YtmsUser.class);

                    user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
                    user.setAccountStatus(UserAccountStatusTypes.PENDING);

                    //reassigned with the new created data
                    user = this
                            .userRepository
                            .save(user);

                    //re-assigning the dto class with the new data
                    userDto = this
                            .modelMapper
                            .map(user, YtmsUserDto.class);
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
        YtmsUser user = null;

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
                YtmsUser ytmsUser = this.userRepository.getUserByEmail(email);
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
        YtmsUser user = this.userRepository.getUserByEmail(newEmail);
        if (user != null && StringUtils.isNotEmpty(password)) {
            user.setPassword(this.passwordEncoder.encode(password));
            System.out.println(" changing password for " + user.toString());
            this.userRepository.save(user);
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

        YtmsUser user = this.userRepository.getUserByEmail(email);

        if (user != null && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(oldPassword)) {
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password));
                this.userRepository.save(user);
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
        List<YtmsUser> allTrainers = this.userRepository.findAllTrainers();
        if (!allTrainers.isEmpty()) {
            return allTrainers
                    .stream()
                    .map(yur -> this
                            .modelMapper
                            .map(yur, YtmsUserDto.class))
                    .toList();
        } else
            throw new ApplicationException("No Trainers found !");
    }

    @Override
    public String SetLoginHistory(String currentUserEmail) {
        YtmsUser currentUser = userRepository.getUserByEmail(currentUserEmail);
        // create new login history
        if (currentUser != null) {
            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setLoginTime(LocalDateTime.now().minusDays(0));
            loginHistory.setUserHistory(currentUser);
            //currentUser.setLastLogin(Instant.now());
            currentUser.addLoginHistory(loginHistory);
            userRepository.save(currentUser);
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
