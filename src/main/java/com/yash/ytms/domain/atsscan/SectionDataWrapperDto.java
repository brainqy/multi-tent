package com.yash.ytms.domain.atsscan;

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
 * @since 15-04-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SectionDataWrapperDto {
    private int wrapper_id;
     private List<SectionDataDto> allData;
    private double finalProgress;
    private String jobTitle;
    private String resume;
    private String jobDescription;
    private String createdAt;
    private  String createdBy;
    private boolean isArchived;
    private  boolean isStarred;
}
