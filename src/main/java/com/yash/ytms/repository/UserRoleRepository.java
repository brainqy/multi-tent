package com.yash.ytms.repository;

import com.yash.ytms.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("select ur from UserRole ur where ur.roleId=?1")
    UserRole getUserRoleByRoleId(Long roleId);

    @Query("select ur from UserRole ur where ur.roleTypes=?1")
    UserRole getUserRoleByRoleName(String roleType);

    @Query("select ur from UserRole ur")
    Set<UserRole> getAllUserRoles();
}
