package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.Bookmark;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.repository.BookmarkRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IBookmarkService;
import jakarta.transaction.Transactional;
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
public class BookmarkServiceImpl implements IBookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private YtmsUserRepository userRepository;
    @Override
    @Transactional
    public Bookmark addBookmark(Principal principal, String articleUrl) {
        String userName=principal.getName();
        YtmsUser user = userRepository.getUserByEmail(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Bookmark bookmark = new Bookmark();
        bookmark.setUserEmail(userName);
        bookmark.setArticleUrl(articleUrl);
        bookmark.setCreatedAt(LocalDateTime.now());

        return bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(Principal principal, String articleId) {
        String username=principal.getName();
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserEmailAndArticleUrl(username, articleId);
        bookmark.ifPresent(bookmarkRepository::delete);

    }
    public List<Bookmark> getUserBookmarks(Principal principal) {
        String userName=principal.getName();
        return bookmarkRepository.findByUserEmail(userName);
    }
}
