package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 06-03-2025
 */
import com.brainqy.api.domain.Bookmark;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.domain.resume.PostType;
import com.brainqy.api.dto.BookmarkDto;
import com.brainqy.api.repository.BookmarkRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.ServiceImpls.IBookmarkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
class IBookmarkServiceImplTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private YtmsUserRepository userRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private IBookmarkServiceImpl bookmarkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBookmark_shouldAddBookmark_whenBookmarkDoesNotExist() {
        String userName = "dvsomwanshi@gmail.com";
        long postId = 1L;

        YtmsUser user = new YtmsUser();
        user.setEmailAdd(userName);

        when(principal.getName()).thenReturn(userName);
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookmarkRepository.findByUserEmailAndPostId(anyString(), anyLong())).thenReturn(Optional.empty());

        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setUserEmail(userName);
        bookmark.setPostId(postId);
        bookmark.setPostType(PostType.BLOGPOST);
        bookmark.setCreatedAt(LocalDateTime.now());

        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

        BookmarkDto result = bookmarkService.addBookmark(postId, principal);

        assertNotNull(result);
        assertEquals(userName, result.getUserEmail());
        assertEquals(postId, result.getPostId());
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    void addBookmark_shouldRemoveBookmark_whenBookmarkExists() {
        String userName = "dvsomwanshi@gmail.com";
        long postId = 1L;

        YtmsUser user = new YtmsUser();
        user.setEmailAdd(userName);

        when(principal.getName()).thenReturn(userName);
        when(userRepository.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        Bookmark existingBookmark = new Bookmark();
        existingBookmark.setId(1L);
        existingBookmark.setUserEmail(userName);
        existingBookmark.setPostId(postId);

        when(bookmarkRepository.findByUserEmailAndPostId(anyString(), anyLong())).thenReturn(Optional.of(existingBookmark));

        BookmarkDto result = bookmarkService.addBookmark(postId, principal);

        assertNotNull(result);
        assertEquals(userName, result.getUserEmail());
        assertEquals(postId, result.getPostId());
        verify(bookmarkRepository, times(1)).deleteById(existingBookmark.getId());
    }

    @Test
    void removeBookmark_shouldRemoveBookmark_whenBookmarkExists() {
        String userName = "dvsomwanshi@gmail.com";
        long postId = 1L;

        when(principal.getName()).thenReturn(userName);

        Bookmark existingBookmark = new Bookmark();
        existingBookmark.setId(1L);
        existingBookmark.setUserEmail(userName);
        existingBookmark.setPostId(postId);

        when(bookmarkRepository.findByUserEmailAndPostId(anyString(), anyLong())).thenReturn(Optional.of(existingBookmark));

        bookmarkService.removeBookmark(postId, principal);

        verify(bookmarkRepository, times(1)).delete(existingBookmark);
    }

    @Test
    void getUserBookmarks_shouldReturnBookmarks_whenBookmarksExist() {
        String userName = "dvsomwanshi@gmail.com";

        when(principal.getName()).thenReturn(userName);

        Bookmark bookmark = new Bookmark();
        bookmark.setId(1L);
        bookmark.setUserEmail(userName);
        bookmark.setPostId(1L);

        when(bookmarkRepository.findByUserEmail(anyString())).thenReturn(List.of(bookmark));

        List<Bookmark> result = bookmarkService.getUserBookmarks(principal);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userName, result.get(0).getUserEmail());
    }
}
