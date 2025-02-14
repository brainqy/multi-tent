package com.brainqy.api.controller;

import com.brainqy.api.domain.forum.ForumDto;
import com.brainqy.api.dto.BookmarkDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.services.IServices.ForumService;
import com.brainqy.api.services.IServices.IBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @Autowired
    private IBookmarkService bookmarkService;
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
    @GetMapping("/getAllBookmarked")
    public ResponseEntity<List<ForumDto>> getAllBookmarkedForumPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Principal principal
    ) {
        List<ForumDto> forumDto = forumService.getAllBookmarkedForumPosts(page, pageSize,principal);
        return ResponseEntity.ok(forumDto);
    }

    @PutMapping("/{id}/star")
    public ResponseEntity<BookmarkDto> saveBookmark(@PathVariable Long id, Principal principal) {
        BookmarkDto updatedEntity = this.bookmarkService.addBookmark(id,principal);
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setData(updatedEntity);
        return new ResponseEntity(wrapperDto,HttpStatus.OK);
    }

}
