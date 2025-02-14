package com.brainqy.api.repository;

import com.brainqy.api.domain.Referral;
import com.brainqy.api.domain.YtmsUser;
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
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    @Query("SELECT r FROM Referral r WHERE r.referrer = :user")
    List<Referral> findByReferrerId(YtmsUser user);
}
