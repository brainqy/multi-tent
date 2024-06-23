package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.Question;
import com.yash.ytms.dto.QuestionDto;
import com.yash.ytms.repository.QuestionRepository;
import com.yash.ytms.services.IServices.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 26-04-2024
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private  QuestionRepository questionRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public QuestionDto createQuestion(QuestionDto questionDto) {
        Question question=modelMapper.map(questionDto, Question.class);
        questionRepository.save(question);
        return modelMapper.map(question,QuestionDto.class);
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto, long questionId) {
        return null;
    }

    @Override
    public void deleteQuestion(long questionId) {

    }

    @Override
    public List<QuestionDto> getALlQuestions() {
       List<Question> questions= questionRepository.findAll();
        List<QuestionDto> questionDtos = questions.stream().map(que -> {
            return modelMapper.map(que, QuestionDto.class);

        }).collect(Collectors.toList());

        return questionDtos;
    }
}
