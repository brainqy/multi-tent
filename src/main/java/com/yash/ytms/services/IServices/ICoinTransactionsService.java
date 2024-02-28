package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.CoinTransaction;
import com.yash.ytms.dto.CoinTransactionDto;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-02-2024
 */
public interface ICoinTransactionsService {
    List<CoinTransactionDto> findTransactionsByUser(String email);
    Integer getUserBalance(String email);
}
