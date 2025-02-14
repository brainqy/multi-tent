package com.brainqy.api.repository;

import com.brainqy.api.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 26-04-2024
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
}
