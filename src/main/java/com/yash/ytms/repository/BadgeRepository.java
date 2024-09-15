package com.yash.ytms.repository;

import com.yash.ytms.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
