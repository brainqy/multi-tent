package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @since 09-03-2025
 * @project multi-tent
 */

import com.brainqy.api.domain.Question;
import com.brainqy.api.dto.QuestionDto;
import com.brainqy.api.repository.QuestionRepository;
import com.brainqy.api.services.ServiceImpls.IQuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IQuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IQuestionServiceImpl questionService;

    private Question question;
    private QuestionDto questionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        question = new Question();
        question.setQuestionId(1L);
        question.setQuestionType("Multiple Choice");
        question.setQuestion("What is Java?");
        question.setOptions(List.of("Programming Language", "Coffee", "Animal"));
        question.setAnswer("Programming Language");
        question.setExplanation("Java is a programming language.");

        questionDto = new QuestionDto();
        questionDto.setQuestionId(1L);
        questionDto.setQuestionType("Multiple Choice");
        questionDto.setQuestion("What is Java?");
        questionDto.setOptions(List.of("Programming Language", "Coffee", "Animal"));
        questionDto.setAnswer("Programming Language");
        questionDto.setExplanation("Java is a programming language.");
    }

    @Test
    void createQuestion_ShouldReturnCreatedQuestion() {
        when(modelMapper.map(questionDto, Question.class)).thenReturn(question);
        when(questionRepository.save(question)).thenReturn(question);
        when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto);

        QuestionDto createdQuestion = questionService.createQuestion(questionDto);

        assertNotNull(createdQuestion);
        assertEquals(questionDto.getQuestionId(), createdQuestion.getQuestionId());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void updateQuestion_ShouldReturnUpdatedQuestion() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(modelMapper.map(questionDto, Question.class)).thenReturn(question);
        when(questionRepository.save(question)).thenReturn(question);
        when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto);

        QuestionDto updatedQuestion = questionService.updateQuestion(questionDto, 1L);

        assertNotNull(updatedQuestion);
        assertEquals(questionDto.getQuestionId(), updatedQuestion.getQuestionId());
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void updateQuestion_ShouldThrowExceptionWhenQuestionNotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> questionService.updateQuestion(questionDto, 1L));
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, never()).save(any());
    }

    @Test
    void deleteQuestion_ShouldDeleteQuestion() {
        when(questionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(questionRepository).deleteById(1L);

        questionService.deleteQuestion(1L);

        verify(questionRepository, times(1)).existsById(1L);
        verify(questionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteQuestion_ShouldThrowExceptionWhenQuestionNotFound() {
        when(questionRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> questionService.deleteQuestion(1L));
        verify(questionRepository, times(1)).existsById(1L);
        verify(questionRepository, never()).deleteById(1L);
    }

    @Test
    void getAllQuestions_ShouldReturnListOfQuestionDtos() {
        when(questionRepository.findAll()).thenReturn(List.of(question));
        when(modelMapper.map(question, QuestionDto.class)).thenReturn(questionDto);

        List<QuestionDto> questionDtos = questionService.getALlQuestions();

        assertNotNull(questionDtos);
        assertFalse(questionDtos.isEmpty());
        assertEquals(1, questionDtos.size());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void getAllQuestions_ShouldReturnEmptyListWhenNoQuestions() {
        when(questionRepository.findAll()).thenReturn(Collections.emptyList());

        List<QuestionDto> questionDtos = questionService.getALlQuestions();

        assertNotNull(questionDtos);
        assertTrue(questionDtos.isEmpty());
        verify(questionRepository, times(1)).findAll();
    }
}
