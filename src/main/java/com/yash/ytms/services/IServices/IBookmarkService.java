package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.Bookmark;

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
    public Bookmark addBookmark(Principal principal, String articleId);
    public void removeBookmark(Principal principal, String articleId);

}
