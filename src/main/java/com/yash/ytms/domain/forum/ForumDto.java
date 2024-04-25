package com.yash.ytms.domain.forum;

import com.yash.ytms.domain.YtmsUser;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 18-04-2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ForumDto {
    private long forum_id;
    private String forum_title;
    private String forum_body;
    private String createdBy;
    private String createdAt;
    private List<String> tags;
}