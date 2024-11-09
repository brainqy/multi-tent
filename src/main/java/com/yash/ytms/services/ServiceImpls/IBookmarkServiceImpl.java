package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.Bookmark;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.domain.resume.PostType;
import com.yash.ytms.repository.BookmarkRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IBookmarkService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 17-09-2024
 */
@Service
public class IBookmarkServiceImpl implements IBookmarkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IBookmarkServiceImpl.class);

    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private YtmsUserRepository userRepository;
    @Override
    @Transactional
    public Bookmark addBookmark(Principal principal, long postId) {
        String userName = principal.getName();
        YtmsUser user = userRepository.getUserByEmail(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the bookmark already exists for the given postId and user
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserEmailAndPostId(userName, postId);

        if (existingBookmark.isPresent()) {
            // If bookmark exists, remove it (unbookmark)
            bookmarkRepository.deleteById(existingBookmark.get().getId());
            return existingBookmark.get(); // Return removed bookmark
        } else {
            // If bookmark doesn't exist, add a new one
            Bookmark bookmark = new Bookmark();
            bookmark.setUserEmail(userName);
            bookmark.setPostId(postId);
            bookmark.setPostType(PostType.BLOGPOST); // Assuming you're bookmarking a blogpost
            bookmark.setCreatedAt(LocalDateTime.now());

            Bookmark savedBookmark = bookmarkRepository.save(bookmark);
            return savedBookmark; // Return the newly created bookmark
        }
    }


    @Override
    @Transactional
    public void removeBookmark(Principal principal, long postId) {
        String username=principal.getName();
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserEmailAndPostId(username, postId);
        bookmark.ifPresent(bookmarkRepository::delete);

    }
    public List<Bookmark> getUserBookmarks(Principal principal) {
        String userName=principal.getName();
        return bookmarkRepository.findByUserEmail(userName);
    }
}
