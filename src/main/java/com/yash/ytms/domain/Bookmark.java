package com.yash.ytms.domain;

import com.yash.ytms.domain.resume.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 17-09-2024
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private String userEmail;


    @JoinColumn(name = "article_id")  // Or job_id depending on what you're bookmarking
    private long postId;

    @JoinColumn(name = "post_type")
    private PostType postType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors, getters, setters
}

