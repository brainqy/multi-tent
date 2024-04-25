package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.forum.ForumDto;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 18-04-2024
 */
public interface ForumService {
    ForumDto createForum(ForumDto forum);
    public ForumDto updateForum(long forumId, ForumDto forumDto);
    public void deleteForum(long forumId);

    ForumDto getForumPost(long forumId);

    List<ForumDto> getAllForumPosts(int page, int pageSize);
}
