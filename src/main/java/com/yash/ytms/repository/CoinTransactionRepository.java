package com.yash.ytms.repository;

import com.yash.ytms.domain.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 13-02-2024
 */
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {
}
