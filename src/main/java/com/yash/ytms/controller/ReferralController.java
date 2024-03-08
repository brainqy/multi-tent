package com.yash.ytms.controller;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yash.ytms.domain.Referral;
import com.yash.ytms.dto.ReferralDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.services.IServices.IReferralService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 19-02-2024
 */
@RestController
@RequestMapping("/refer")
public class ReferralController {
    @Autowired
    IYtmsUserService userService;
    @Autowired
    IReferralService referralService;
    @Value("${server.port}")
    String port;


@PostMapping("/resendEmail")
    public ResponseEntity resendEmail(@RequestBody String referralEmail) throws MessagingException {
    ResponseEntity dto = this.referralService.sendEmailToReferral(referralEmail);
        return new ResponseEntity(dto,HttpStatus.OK);
    }
    @GetMapping("/getReferrals")
    public ResponseEntity<List<ReferralDto>> getReferrals(Authentication authentication) {

        // Get the authenticated user's email
        String userEmail = authentication.getName();

        // Retrieve user details based on email
        YtmsUserDto userDto = userService.getUserByEmailAdd(userEmail);

        // Generate referral link
        String referralLink = "http://localhost:8083/register?ref=" + userDto.getEmailAdd();

        // Retrieve user's referrals
        List<ReferralDto> myReferrals = referralService.findByReferrerId(userDto);
        System.out.println(myReferrals.toString());

        // Return the list of referrals
        return new ResponseEntity<>(myReferrals, HttpStatus.OK);
    }
    @GetMapping("/register")
    public void acceptReferral(@RequestBody Map<String, String> map){
    System.out.println(map.toString());

    }
    @GetMapping("/getReferralLink")
    public ResponseEntity getMyReferralLink(){
    String enCodedEmail=  referralService.getMyReferralLink();
        String referralLink = "http://localhost:"+port+"/register?ref="+enCodedEmail;
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new JsonPrimitive(referralLink));

        // Return JSON object with HTTP status OK
        return new  ResponseEntity(jsonResponse ,HttpStatus.OK);
    }

}
