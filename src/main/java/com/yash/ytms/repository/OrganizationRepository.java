package com.yash.ytms.repository;

import com.yash.ytms.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 14-02-2024
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}