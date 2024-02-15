package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.constants.AppConstants;
import com.yash.ytms.constants.RequestStatusTypes;
import com.yash.ytms.constants.UserAccountStatusTypes;
import com.yash.ytms.dto.DailyStreakDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.security.jwt.JwtAuthRequest;
import com.yash.ytms.security.jwt.JwtAuthResponse;
import com.yash.ytms.security.jwt.JwtTokenHelper;
import com.yash.ytms.security.userdetails.CustomUserDetails;
import com.yash.ytms.security.userdetails.CustomUserDetailsServiceImpl;
import com.yash.ytms.services.IServices.IAuthService;
import com.yash.ytms.services.IServices.IDailyStreakService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private JwtTokenHelper tokenHelper;
    @Autowired
    private IYtmsUserService userService;
    @Autowired
    private IDailyStreakService dailyStreakService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Override
    public JwtAuthResponse login(JwtAuthRequest authRequest) {
        String userName = authRequest.getEmail();
        String password = authRequest.getPassword();
        String token = null;

        this.authenticate(userName, password);

        CustomUserDetails userDetails = this
                .userDetailsService
                .loadUserByUsername(userName);

        Assert.notNull(userDetails);
        JwtAuthResponse authResponse = new JwtAuthResponse();

        if (StringUtils.equalsIgnoreCase(userDetails.getAccountStatus().toString(),
                UserAccountStatusTypes.APPROVED.toString())) {

            token = this
                    .tokenHelper
                    .generateToken(userDetails);

            authResponse.setToken(token);
            authResponse.setMessage("Successfully Logged In");
            this.userService.SetLoginHistory(userDetails.getEmailAdd());
            DailyStreakDto dailyStreak = this.dailyStreakService.getDailyStreak(userDetails.getEmailAdd());
            authResponse.setDailyStreakDto(dailyStreak);

            authResponse.setStatus(RequestStatusTypes.SUCCESS.toString());

        } else if (StringUtils.equalsIgnoreCase(userDetails.getAccountStatus().toString(),
                UserAccountStatusTypes.DECLINED.toString())) {

            authResponse.setToken(null);
            authResponse.setStatus(RequestStatusTypes.FAILED.toString());
            authResponse.setMessage(AppConstants.DECLINED_USER_MESSAGE);
        } else {
            authResponse.setToken(null);
            authResponse.setStatus(RequestStatusTypes.FAILED.toString());
            authResponse.setMessage("Your request is in Pending state for Approval");
        }
        return authResponse;
    }

    private void authenticate(String userName,
                              String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException credentialsException) {
            throw new ApplicationException("Invalid username or password");
        }
    }
}
