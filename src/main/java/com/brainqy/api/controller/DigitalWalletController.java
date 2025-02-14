package com.brainqy.api.controller;

import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.services.IServices.ICoinTransactionsService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class DigitalWalletController {
    @Autowired
    IYtmsUserService userService;
    @Autowired
    ICoinTransactionsService coinTransactionsService;
    @GetMapping("/wallet")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        YtmsUserDto userDto = userService.getUserByEmailAdd(authentication.getName());
        ResponseWrapperDto transactions = coinTransactionsService.findTransactionsByUser(userDto.getEmailAdd());

        return  new ResponseEntity(transactions, HttpStatus.OK);
    }
}
