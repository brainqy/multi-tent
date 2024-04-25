package com.yash.ytms.domain.forum;

import com.yash.ytms.domain.YtmsUser;
import groovy.lang.GString;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
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
@Entity
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long forum_id;
    @NotNull
    private String forum_title;
    @Column(length = 10000)
    @NotNull
    private String forum_body;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<String> tags;
}
