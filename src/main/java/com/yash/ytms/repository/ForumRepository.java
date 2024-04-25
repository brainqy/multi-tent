package com.yash.ytms.repository;

import com.yash.ytms.domain.forum.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 18-04-2024
 */
@Repository
public interface ForumRepository extends JpaRepository<Forum,Long> {
}
