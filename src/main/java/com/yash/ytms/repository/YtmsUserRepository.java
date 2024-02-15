package com.yash.ytms.repository;

import com.yash.ytms.domain.YtmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Repository
public interface YtmsUserRepository extends JpaRepository<YtmsUser, Long> {

    @Query("select yur from YtmsUser yur where yur.emailAdd=?1")
    YtmsUser getUserByEmail(String email);

    @Query("select yur from YtmsUser yur where yur.accountStatus=com.yash.ytms.constants.UserAccountStatusTypes.PENDING")
    List<YtmsUser> getAllPendingUsers();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update YtmsUser yur set yur.accountStatus=com.yash.ytms.constants.UserAccountStatusTypes.APPROVED where yur.emailAdd=?1")
    Integer approvePendingUser(String emailAdd);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update YtmsUser yur set yur.accountStatus=com.yash.ytms.constants.UserAccountStatusTypes.DECLINED where yur.emailAdd=?1")
    Integer declinePendingUser(String emailAdd);

    @Query("select yur from YtmsUser yur where yur.userRole.roleTypes='ROLE_TRAINER'")
    List<YtmsUser> findAllTrainers();
}
