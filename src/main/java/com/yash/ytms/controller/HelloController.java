package com.yash.ytms.controller;

import com.yash.ytms.domain.forum.ForumDto;
import com.yash.ytms.dto.HelloDto;
import com.yash.ytms.services.IServices.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 23-06-2024
 */
@RestController
@RequestMapping
public class HelloController {
    @Autowired
    private ForumService forumService;
    @GetMapping("/hello")
    public HelloDto getHello(){
        return   new HelloDto("Hello User in branch") ;
    }

}
