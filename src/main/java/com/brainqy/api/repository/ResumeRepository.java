package com.brainqy.api.repository;

import com.brainqy.api.domain.resume.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    @Query("SELECT r FROM Resume r WHERE r.createdBy=:userEmail")
    List<Resume> getAllResumesByEmail(String userEmail);
    // Additional query methods if needed
}
