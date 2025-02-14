package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.BookmarkDto;
import jakarta.transaction.Transactional;

import java.security.Principal;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 17-09-2024
 */
public interface IBookmarkService {

    @Transactional
    BookmarkDto addBookmark(long postId, Principal principal);

    public void removeBookmark(long postId, Principal principal);

}
