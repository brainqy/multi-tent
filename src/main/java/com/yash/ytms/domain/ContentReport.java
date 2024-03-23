package com.yash.ytms.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-03-2024
 */
    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "report_content")
    public class ContentReport {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String contentType;
        private List<String> tags;
        private String reportedBy;
        private double rating;
        private String comment;
        private Long contentId;
        private String link;
        private double avgRating;

    }
