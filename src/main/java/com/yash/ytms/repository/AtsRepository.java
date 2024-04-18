package com.yash.ytms.repository;

import com.yash.ytms.domain.atsscan.SectionDataWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 16-04-2024
 */
@Repository
public interface AtsRepository extends JpaRepository<SectionDataWrapper,Long> {
    SectionDataWrapper findFirstByOrderByCreatedAtDesc();
}
