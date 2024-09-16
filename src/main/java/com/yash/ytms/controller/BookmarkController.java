package com.yash.ytms.controller;

import com.yash.ytms.domain.Bookmark;
import com.yash.ytms.services.ServiceImpls.BookmarkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BookmarkServiceImpl bookmarkService;

    @PostMapping("/add")
    public ResponseEntity<Void> addBookmark(@RequestParam Long userId, @RequestParam String articleId, Principal principal) {
        bookmarkService.addBookmark(principal, articleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBookmark(@RequestParam String articleId,Principal principal) {
        bookmarkService.removeBookmark(principal, articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bookmark>> getUserBookmarks(Principal userId) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(userId));
    }
}

