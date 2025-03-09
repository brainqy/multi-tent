package com.brainqy.api.services.ServiceImpls;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.domain.forum.Forum;
import com.brainqy.api.domain.forum.ForumDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.ForumRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.IServices.ForumService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IForumImplTest {

    @InjectMocks
    private IForumImpl forumService;

    @Mock
    private ForumRepository forumRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private YtmsUserRepository userRepository;

    private Forum forum;
    private ForumDto forumDto;


    @BeforeEach
    void setUp() {
        forum = new Forum();
        forum.setForum_id(1L);
        forum.setForum_title("Test Forum");
        forum.setForum_body("Test body");
        forum.setCreatedAt(LocalDateTime.now());
        forum.setCreatedBy("testuser@example.com");

        forumDto = new ForumDto();
        forumDto.setForum_title("Test Forum");
        forumDto.setForum_body("Test body");

        Mockito.lenient().when(modelMapper.map(forumDto, Forum.class)).thenReturn(forum);
        Mockito.lenient().when(modelMapper.map(forum, ForumDto.class)).thenReturn(forumDto);
    }

    @Test
    void createForum_ShouldReturnForumDto() {
        when(forumRepo.save(any(Forum.class))).thenReturn(forum);

        ForumDto createdForum = forumService.createForum(forumDto);

        assertNotNull(createdForum);
        assertEquals(forumDto.getForum_title(), createdForum.getForum_title());
        verify(forumRepo, times(1)).save(any(Forum.class));
    }

    @Test
    void updateForum_ShouldUpdateAndReturnForumDto() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.of(forum));
        when(forumRepo.save(any(Forum.class))).thenReturn(forum);

        ForumDto updatedForum = forumService.updateForum(forumId, forumDto);

        assertNotNull(updatedForum);
        assertEquals(forumDto.getForum_title(), updatedForum.getForum_title());
        verify(forumRepo, times(1)).save(any(Forum.class));
    }

    @Test
    void updateForum_WhenForumNotFound_ShouldThrowException() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> forumService.updateForum(forumId, forumDto));
        verify(forumRepo, never()).save(any(Forum.class));
    }

    @Test
    void deleteForum_ShouldDeleteForum() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.of(forum));

        forumService.deleteForum(forumId);

        verify(forumRepo, times(1)).delete(forum);
    }

    @Test
    void deleteForum_WhenForumNotFound_ShouldThrowException() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> forumService.deleteForum(forumId));
        verify(forumRepo, never()).delete(any(Forum.class));
    }

    @Test
    void getForumPost_ShouldReturnForumDto() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.of(forum));

        ForumDto result = forumService.getForumPost(forumId);

        assertNotNull(result);
        assertEquals(forumDto.getForum_title(), result.getForum_title());
    }

    @Test
    void getForumPost_WhenForumNotFound_ShouldReturnNull() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.empty());

        ForumDto result = forumService.getForumPost(forumId);

        assertNull(result);
    }

    @Test
    void saveAsBookmarked_ShouldToggleBookmarkStatus() {
        long forumId = 1L;
        forum.setBookmarked(false);
        when(forumRepo.findById(forumId)).thenReturn(Optional.of(forum));
        when(forumRepo.save(any(Forum.class))).thenReturn(forum);

        ForumDto result = forumService.saveAsBookmarked(forumId);

        assertNotNull(result);
        assertTrue(forum.isBookmarked());
        verify(forumRepo, times(1)).save(forum);
    }

    @Test
    void saveAsBookmarked_WhenForumNotFound_ShouldThrowException() {
        long forumId = 1L;
        when(forumRepo.findById(forumId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> forumService.saveAsBookmarked(forumId));
        verify(forumRepo, never()).save(any(Forum.class));
    }

    @Test
    void getAllForumPosts_ShouldReturnListOfForumDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Forum> forumPage = new PageImpl<>(List.of(forum));
        when(forumRepo.findAll(pageable)).thenReturn(forumPage);
        List<ForumDto> forums = forumService.getAllForumPosts(0, 10);
        assertNotNull(forums);
        assertFalse(forums.isEmpty());
        verify(forumRepo, times(1)).findAll(pageable);
    }
}
