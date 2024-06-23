package com.yash.ytms.controller;

import com.yash.ytms.dto.QuestionDto;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.services.IServices.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 26-04-2024
 */
@RestController
@RequestMapping("/quiz")
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @PostMapping("/create-question")
    public ResponseEntity createQuestion(@RequestBody QuestionDto questionDto){
      QuestionDto questionCreated=  questionService.createQuestion(questionDto);
      return  new ResponseEntity<>(questionCreated,HttpStatus.CREATED);
    }
    @GetMapping("/get-all-questions")
    public   ResponseEntity getAllQuestions(){
        List<QuestionDto> questionsFetched= questionService.getALlQuestions();
        return  new ResponseEntity(questionsFetched,HttpStatus.OK);
    }
}
