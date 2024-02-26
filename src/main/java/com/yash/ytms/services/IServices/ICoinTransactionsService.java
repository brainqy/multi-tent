package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.CoinTransaction;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.YtmsUserDto;
import jakarta.transaction.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 19-02-2024
 */
public interface ICoinTransactionsService {
    ResponseEntity<?> getWallet(Authentication authentication);
    List<CoinTransaction> findTransactionsByUser(String Email);
}
