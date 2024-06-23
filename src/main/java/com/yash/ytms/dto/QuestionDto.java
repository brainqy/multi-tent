package com.yash.ytms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 26-04-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private long questionId;
    private String questionType;
    private String question;
    private List<String> options;
    private String answer;
    private List<String> comments;
    private String explanation;
    private  List<String> tags;
}
