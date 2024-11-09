package com.yash.ytms.controller;

import com.yash.ytms.domain.Bookmark;
import com.yash.ytms.domain.resume.ResumeDto;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.services.ServiceImpls.IBookmarkServiceImpl;
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
        bookmarkService.addBookmark(principal, postId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/star")
    public ResponseEntity<ResumeDto> saveAsStarred(@PathVariable Long id,Principal principal) {
        Bookmark updatedEntity = this.bookmarkService.addBookmark(principal, id);
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setData(updatedEntity);
        return new ResponseEntity(wrapperDto, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@RequestParam long articleId,Principal principal) {
        bookmarkService.removeBookmark(principal, articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bookmark>> getUserBookmarks(Principal userId) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(userId));
    }
}

