package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.Bookmark;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.domain.resume.PostType;
import com.brainqy.api.dto.BookmarkDto;
import com.brainqy.api.repository.BookmarkRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.IServices.IBookmarkService;
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
    public BookmarkDto addBookmark(long postId, Principal principal) {
        String userName = principal.getName();
        YtmsUser user = userRepository.getUserByEmail(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the bookmark already exists for the given postId and user
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserEmailAndPostId(userName, postId);

        if (existingBookmark.isPresent()) {
            // If bookmark exists, remove it (unbookmark)
            bookmarkRepository.deleteById(existingBookmark.get().getId());
            return mapToDto(existingBookmark.get()); // Convert and return removed bookmark as DTO
        } else {
            // If bookmark doesn't exist, add a new one
            Bookmark bookmark = new Bookmark();
            bookmark.setUserEmail(userName);
            bookmark.setPostId(postId);
            bookmark.setPostType(PostType.BLOGPOST); // Assuming you're bookmarking a blogpost
            bookmark.setCreatedAt(LocalDateTime.now());

            Bookmark savedBookmark = bookmarkRepository.save(bookmark);
            return mapToDto(savedBookmark); // Convert and return the newly created bookmark as DTO
        }
    }

    // Utility method to convert Bookmark entity to BookmarkDto
    private BookmarkDto mapToDto(Bookmark bookmark) {
        BookmarkDto dto = new BookmarkDto();
        dto.setId(bookmark.getId());
        dto.setUserEmail(bookmark.getUserEmail());
        dto.setPostId(bookmark.getPostId());
        dto.setPostType(bookmark.getPostType());
        dto.setCreatedAt(bookmark.getCreatedAt());
        return dto;
    }


    @Override
    @Transactional
    public void removeBookmark(long postId, Principal principal) {
        String username=principal.getName();
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserEmailAndPostId(username, postId);
        bookmark.ifPresent(bookmarkRepository::delete);

    }
    public List<Bookmark> getUserBookmarks(Principal principal) {
        String userName=principal.getName();
        return bookmarkRepository.findByUserEmail(userName);
    }
}
