package com.yash.ytms.repository;

import com.yash.ytms.domain.Referral;
import com.yash.ytms.domain.UserRole;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.ReferralDto;
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
