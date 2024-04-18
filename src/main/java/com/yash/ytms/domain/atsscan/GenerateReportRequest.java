package com.yash.ytms.domain.atsscan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class GenerateReportRequest {
    private String resume;
    private String jobDescription;
    private String randomString;
}
