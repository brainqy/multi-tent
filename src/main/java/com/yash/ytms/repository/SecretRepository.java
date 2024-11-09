package com.yash.ytms.repository;

import com.yash.ytms.domain.resume.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.security.Principal;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-11-2024
 */
public interface SecretRepository extends JpaRepository<Secret,Long> {
    @Query("SELECT s FROM Secret s WHERE s.secretKey = :key AND s.owner.emailAdd = :ownerEmail")
    Secret findBySecretKey(String key, String ownerEmail);
}
