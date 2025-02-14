package com.brainqy.api.repository;

import com.brainqy.api.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 14-09-2024
 */
@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByNameIgnoreCase(String name);
    //@Query("SELECT b FROM Badge b JOIN b.users u WHERE u.username = :username")
    @Query("SELECT b FROM Badge b JOIN b.users u WHERE u.emailAdd = :emailAdd")
    List<Badge> getAssignedBadgesByUserName(@Param("emailAdd") String emailAdd);

}
