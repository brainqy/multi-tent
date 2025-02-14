package com.brainqy.api.repository;

import com.brainqy.api.domain.Job;
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
 * @since 29-05-2024
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByStatus(String status);
    @Query("SELECT r FROM Job r WHERE r.createdBy=:userEmail")

    List<Job> getJobsByEmail(String userEmail);
    // Additional query methods if needed
}