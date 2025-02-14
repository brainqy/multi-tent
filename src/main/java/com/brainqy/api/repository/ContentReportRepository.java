package com.brainqy.api.repository;

import com.brainqy.api.domain.ContentReport;
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
 * @since 22-03-2024
 */
@Repository
public interface ContentReportRepository extends JpaRepository<ContentReport,Long> {
    @Query("SELECT r FROM ContentReport r WHERE r.link = :link")
    List<ContentReport> getContentReportyLink(String link);
}
