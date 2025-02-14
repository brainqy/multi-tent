package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.constants.AppConstants;
import com.brainqy.api.constants.RequestStatusTypes;
import com.brainqy.api.constants.UserAccountStatusTypes;
import com.brainqy.api.dto.DailyStreakDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.security.jwt.JwtAuthRequest;
import com.brainqy.api.security.jwt.JwtAuthResponse;
import com.brainqy.api.security.jwt.JwtTokenHelper;
import com.brainqy.api.security.userdetails.CustomUserDetails;
import com.brainqy.api.security.userdetails.CustomUserDetailsServiceImpl;
import com.brainqy.api.services.IServices.IAuthService;
import com.brainqy.api.services.IServices.IDailyStreakService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.internal.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class IAuthServiceImpl implements IAuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IAuthServiceImpl.class);


    @Autowired
    private JwtTokenHelper tokenHelper;
    @Autowired
    private HttpServletResponse response;
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

            // Create a cookie to store the token
            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(true); // Ensures cookie is not accessible through JavaScript
            cookie.setMaxAge(24 * 60 * 60); // Set cookie expiry time (in seconds), here it's set to 1 day
            cookie.setPath("/"); // Set cookie path

            response.addCookie(cookie); // Add the cookie to the response

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
