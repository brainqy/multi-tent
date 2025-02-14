package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.ResponseWrapperDto;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-02-2024
 */
public interface ICoinTransactionsService {
    ResponseWrapperDto findTransactionsByUser(String email);
    Integer getUserBalance(String email);

}
