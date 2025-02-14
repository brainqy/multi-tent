package com.brainqy.api.dto;

import com.brainqy.api.domain.resume.PostType;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {

    private Long id;
    private String userEmail;
    private long postId;
    private PostType postType;
    private LocalDateTime createdAt;

    // Constructors, getters, setters
}

