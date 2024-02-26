package com.yash.ytms.controller;

import com.yash.ytms.domain.Referral;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 19-02-2024
 */
@RestController
@RequestMapping("/org")
public class ReferralController {
    @Autowired
    IYtmsUserService userService;
    @GetMapping("/getReferrals")
    public  List<Referral> getReferrals(Authentication authentication) {
        // Get the authenticated user's email
        String userEmail = authentication.getName();

        // Retrieve user details based on email
        YtmsUserDto userDto = userService.getUserByEmailAdd(userEmail);
         // Generate referral link
        String referralLink = "http://localhost:8083/register?ref=" + userDto.getEmailAdd();

        // Retrieve user's referrals
        List<Referral> myReferrals = userDto.getReferrals();
        System.out.println(myReferrals.toString());

        // Return the list of referrals
        return myReferrals;
    }
}
