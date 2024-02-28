package com.yash.ytms.repository;

import com.yash.ytms.domain.CoinTransaction;
import com.yash.ytms.domain.Referral;
import com.yash.ytms.domain.YtmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 13-02-2024
 */
public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {
    @Query("SELECT r FROM CoinTransaction r WHERE r.user = :user")
    List<CoinTransaction> findByUser(YtmsUser user);
}
