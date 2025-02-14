package com.brainqy.api.controller;

import com.brainqy.api.domain.Bookmark;
import com.brainqy.api.domain.resume.ResumeDto;
import com.brainqy.api.dto.BookmarkDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.services.ServiceImpls.IBookmarkServiceImpl;
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
 * @since 17-09-2024
 */
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private IBookmarkServiceImpl bookmarkService;

    @PostMapping("/add")
    public ResponseEntity<Void> addBookmark(@RequestParam Long postId, @RequestParam String articleId, Principal principal) {
        bookmarkService.addBookmark(postId,principal);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/star")
    public ResponseEntity<ResumeDto> saveAsStarred(@PathVariable Long id, Principal principal) {
        BookmarkDto updatedEntity = this.bookmarkService.addBookmark(id,principal);
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setData(updatedEntity);
        return new ResponseEntity(wrapperDto, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@RequestParam long articleId,Principal principal) {
        bookmarkService.removeBookmark(articleId,principal);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bookmark>> getUserBookmarks(Principal userId) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(userId));
    }
}

