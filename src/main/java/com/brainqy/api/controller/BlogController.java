package com.brainqy.api.controller;

import com.brainqy.api.domain.forum.ForumDto;
import com.brainqy.api.dto.HelloDto;
import com.brainqy.api.exception.JwtTokenExpiredException;
import com.brainqy.api.services.IServices.ForumService;
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
public class BlogController {
    @Autowired
    private ForumService forumService;
    @GetMapping("/hello")
    public HelloDto getHello(){
        throw  new JwtTokenExpiredException("I am thowing in controller layer");
        //return   new HelloDto("Hello User in branch") ;
    }
    @GetMapping("/posts")
    public ResponseEntity<List<ForumDto>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        List<ForumDto> forumDto = forumService.getAllPosts(page, pageSize);
        return ResponseEntity.ok(forumDto);
    }
    @GetMapping("/posts/{forumId}")
    public ResponseEntity<ForumDto> getBlogPost(@PathVariable long forumId) {
        ForumDto forumDto = forumService.getForumPost(forumId);
        if (forumDto != null) {
            return ResponseEntity.ok(forumDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
