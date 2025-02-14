package com.brainqy.api.dto;

import com.brainqy.api.domain.TransactionType;
import com.brainqy.api.domain.SourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-02-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinTransactionDto {

    private int id;
    private YtmsUserDto user;
    private TransactionType transactionType;
    private Integer amount;
    private SourceType sourceType;
    private Date createdDate ;
}
