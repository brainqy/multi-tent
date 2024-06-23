package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.QuestionDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 26-04-2024
 */
public interface QuestionService {
    QuestionDto createQuestion(QuestionDto questionDto);
    QuestionDto updateQuestion(QuestionDto questionDto,long questionId);
    void deleteQuestion(long questionId);
    List<QuestionDto> getALlQuestions();

}
