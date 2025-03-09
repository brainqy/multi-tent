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
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
@Service
public class IAuthServiceImpl implements IAuthService {
    private static final Logger lOGGER = LoggerFactory.getLogger(IAuthServiceImpl.class);


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
    void authenticate(String userName, String password) {
        lOGGER.info("Authenticating user: {}", userName);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userName, password);

        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            lOGGER.info("Authentication successful for user: {}", userName);
        } catch (BadCredentialsException credentialsException) {
            lOGGER.error("Invalid credentials for user: {}", userName);
            throw new ApplicationException("Invalid username or password");
        }
    }

    @Override
    public JwtAuthResponse login(JwtAuthRequest authRequest) {
        String userName = authRequest.getEmail();
        String password = authRequest.getPassword();
        String token = null;

        lOGGER.info("Login attempt for user: {}", userName);

        this.authenticate(userName, password);
        lOGGER.info("User {} authenticated successfully", userName);

        CustomUserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
        if (userDetails == null) {
            throw new ApplicationException("Invalid username or password");
        }
        lOGGER.info("Loaded user details for: {}", userName);

        JwtAuthResponse authResponse = new JwtAuthResponse();

        if (StringUtils.equalsIgnoreCase(userDetails.getAccountStatus().toString(),
                UserAccountStatusTypes.APPROVED.toString())) {

            token = this.tokenHelper.generateToken(userDetails);
            authResponse.setToken(token);

            lOGGER.info("Token generated successfully for user: {}", userName);

            // Create a cookie to store the token
            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setPath("/");

            response.addCookie(cookie);
            lOGGER.info("JWT token added to response as HttpOnly cookie");

            authResponse.setMessage("Successfully Logged In");

            this.userService.SetLoginHistory(userDetails.getEmailAdd());
            lOGGER.info("Login history updated for user: {}", userName);

            DailyStreakDto dailyStreak = this.dailyStreakService.getDailyStreak(userDetails.getEmailAdd());
            authResponse.setDailyStreakDto(dailyStreak);
            lOGGER.info("Daily streak details retrieved for user: {}", userName);

            authResponse.setStatus(RequestStatusTypes.SUCCESS.toString());

        } else if (StringUtils.equalsIgnoreCase(userDetails.getAccountStatus().toString(),
                UserAccountStatusTypes.DECLINED.toString())) {

            authResponse.setToken(null);
            authResponse.setStatus(RequestStatusTypes.FAILED.toString());
            authResponse.setMessage(AppConstants.DECLINED_USER_MESSAGE);
            lOGGER.warn("Login attempt failed for user {} - Account is declined", userName);

        } else {
            authResponse.setToken(null);
            authResponse.setStatus(RequestStatusTypes.FAILED.toString());
            authResponse.setMessage("Your request is in Pending state for Approval");
            lOGGER.warn("Login attempt failed for user {} - Account is pending approval", userName);
        }

        return authResponse;
    }
}
