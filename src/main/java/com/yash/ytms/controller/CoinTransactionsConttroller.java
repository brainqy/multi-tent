package com.yash.ytms.controller;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.yash.ytms.dto.CoinTransactionDto;
import com.yash.ytms.services.IServices.ICoinTransactionsService;
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
 * @project multi-tent
 * @since 22-02-2024
 */
@RestController
@RequestMapping("/coins")
public class CoinTransactionsConttroller {
    @Autowired
    private ICoinTransactionsService coinTransactionsService;
    @GetMapping("/getTransactions")
    public ResponseEntity<List<CoinTransactionDto>> getCoinTransactions(Authentication authentication){
        String currentUserEmail= authentication.getName();
        List<CoinTransactionDto> transations = coinTransactionsService.findTransactionsByUser(currentUserEmail);

        return  new ResponseEntity<>(transations, HttpStatus.OK);
    }
    @GetMapping("/getUserBalance")
    public ResponseEntity getUserBalance(Authentication authentication){
        String currentUserEmail= authentication.getName();
        Integer currentBalance = this.coinTransactionsService.getUserBalance(currentUserEmail);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new JsonPrimitive(currentBalance));
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
}
