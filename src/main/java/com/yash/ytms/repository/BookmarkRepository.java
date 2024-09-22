package com.yash.ytms.repository;

import com.yash.ytms.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 17-09-2024
 */
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserEmail(String userEmail);

    Optional<Bookmark> findByUserEmailAndPostId(String username, long postId);
}
