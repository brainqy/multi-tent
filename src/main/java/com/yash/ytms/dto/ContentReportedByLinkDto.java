package com.yash.ytms.dto;

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
public class ContentReportedByLinkDto {
    private List<String> tags;
    private  List<String> reportedBy;
    private  List<String> comments;
    private  List<String> contentType;
    private  double avgRating;
}
