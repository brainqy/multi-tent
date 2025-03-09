package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.Question;
import com.brainqy.api.dto.QuestionDto;
import com.brainqy.api.repository.QuestionRepository;
import com.brainqy.api.services.IServices.QuestionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.brainqy.api.domain.Question;
import com.brainqy.api.dto.QuestionDto;
import com.brainqy.api.repository.QuestionRepository;
import com.brainqy.api.services.IServices.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IQuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionDto createQuestion(QuestionDto questionDto) {
        Question question = modelMapper.map(questionDto, Question.class);
        questionRepository.save(question);
        return modelMapper.map(question, QuestionDto.class);
    }

    @Override
    public QuestionDto updateQuestion(QuestionDto questionDto, long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(NoSuchElementException::new);
        modelMapper.map(questionDto, question);
        questionRepository.save(question);
        return modelMapper.map(question, QuestionDto.class);
    }

    @Override
    public void deleteQuestion(long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new NoSuchElementException();
        }
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<QuestionDto> getALlQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(question -> modelMapper.map(question, QuestionDto.class))
                .collect(Collectors.toList());
    }
}