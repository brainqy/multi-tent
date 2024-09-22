package com.yash.ytms.repository;

import com.yash.ytms.domain.resume.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-09-2024
 */
@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    // Additional query methods if needed
}
