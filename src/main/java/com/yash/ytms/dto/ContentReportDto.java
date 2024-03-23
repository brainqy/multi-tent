package com.yash.ytms.dto;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentReportDto {
    private String contentType;
    private List<String> tags;
    private String reportedBy;
    private int rating;
    private String comment;
    private Long contentId;
    private String link;
}
