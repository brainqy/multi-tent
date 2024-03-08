package com.yash.ytms.controller;

import com.yash.ytms.domain.CoinTransaction;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.services.IServices.ICoinTransactionsService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DigitalWalletController {
    @Autowired
    IYtmsUserService userService;
    @Autowired
    ICoinTransactionsService coinTransactionsService;
    @GetMapping("/wallet")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        YtmsUserDto userDto = userService.getUserByEmailAdd(authentication.getName());
        List<CoinTransaction> transactions = coinTransactionsService.findTransactionsByUser(userDto.getEmailAdd());

        return ResponseEntity.ok(transactions);
    }
}
