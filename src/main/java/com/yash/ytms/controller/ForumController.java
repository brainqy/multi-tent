package com.yash.ytms.controller;

import com.yash.ytms.domain.forum.ForumDto;
import com.yash.ytms.services.IServices.ForumService;
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
 * @since 18-04-2024
 */
@RestController
@RequestMapping("/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;
    @PostMapping("/createForum")
    public ResponseEntity<ForumDto> createForumPost(@RequestBody ForumDto forumDto){
        forumService.createForum(forumDto);
        return  new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/{forumId}")
    public ResponseEntity<ForumDto> updateForum(@PathVariable long forumId, @RequestBody ForumDto forumDto) {
        ForumDto updatedForum = forumService.updateForum(forumId, forumDto);
        return ResponseEntity.ok(updatedForum);
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> deleteForum(@PathVariable long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDto> getForum(@PathVariable long forumId) {
        ForumDto forumDto = forumService.getForumPost(forumId);
        if (forumDto != null) {
            return ResponseEntity.ok(forumDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<ForumDto>> getAllForumPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        List<ForumDto> forumDto = forumService.getAllForumPosts(page, pageSize);
        return ResponseEntity.ok(forumDto);
    }

}
