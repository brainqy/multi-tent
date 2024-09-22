package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.domain.forum.Forum;
import com.yash.ytms.domain.forum.ForumDto;
import com.yash.ytms.domain.resume.Resume;
import com.yash.ytms.domain.resume.ResumeDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.ForumRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.ForumService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 18-04-2024
 */
@Service
public class ForumImpl implements ForumService {
    @Autowired
    private ForumRepository forumRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private YtmsUserRepository userRepository;
    @Override
    public ForumDto createForum(ForumDto forumDto) {
        Forum forumEntity = modelMapper.map(forumDto, Forum.class);
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String currentUser= auth.getName();
        LocalDateTime timeNow= LocalDateTime.now();
        forumEntity.setCreatedAt(timeNow);
        forumEntity.setCreatedBy("dvsomwanshi@gmail.com");
         forumRepo.save(forumEntity);
         return modelMapper.map(forumEntity,ForumDto.class);
    }
    @Override
    public ForumDto updateForum(long forumId, ForumDto forumDto) {
        Forum existingForum = forumRepo.findById(forumId)
                .orElseThrow(() -> new ApplicationException("Forum not found with id: " + forumId));

        // Update the existing forum entity with the new data
        existingForum.setForum_title(forumDto.getForum_title());
        existingForum.setForum_body(forumDto.getForum_body());
        existingForum.setCreatedAt(LocalDateTime.now()); // Update the timestamp

        // Save the updated forum entity
        forumRepo.save(existingForum);

        // Return the updated forum DTO
        return modelMapper.map(existingForum, ForumDto.class);
    }

    @Override
    public void deleteForum(long forumId) {
        Forum existingForum = forumRepo.findById(forumId)
                .orElseThrow(() -> new ApplicationException("Forum post not found with id: " + forumId));

        // Delete the forum entity
        forumRepo.delete(existingForum);
    }

    @Override
    public ForumDto getForumPost(long forumId) {
        Optional<Forum> forumOptional = forumRepo.findById(forumId);
        if (forumOptional.isPresent()) {
            Forum forum = forumOptional.get();
            return modelMapper.map(forum, ForumDto.class);
        } else {
            return null;
        }
    }
    @Override
    public List<ForumDto> getAllForumPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Forum> forumPage = forumRepo.findAll(pageable);
        List<ForumDto> forums = forumPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Update createdBy to full name
        forums.forEach(forumDto -> {
            String fullName = userRepository.getUserByEmail(forumDto.getCreatedBy()).get() .getFullName();

            forumDto.setCreatedBy(fullName);
        });

        return forums;
    }

    @Override
    public List<ForumDto> getAllPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Forum> forumPage = forumRepo.findAll(pageable);
        List<ForumDto> forums = forumPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return forums;
    }
    @Override
    public ForumDto saveAsBookmarked(Long id) {
        Optional<Forum> optionalEntity = forumRepo.findById(id);
        if (optionalEntity.isPresent()) {
            Forum entity = optionalEntity.get();
            if(entity.isBookmarked()){
                entity.setBookmarked(false);
            }else {
                entity.setBookmarked(true);
            }
            forumRepo.save(entity);
            ForumDto entityDto = modelMapper.map(entity, ForumDto.class);
            return entityDto;
        }
        throw new EntityNotFoundException("SectionDataWrapperDto not found with id: " + id);

    }

    @Override
    public List<ForumDto> getAllBookmarkedForumPosts(int page, int pageSize, Principal principal) {
        String userEmail=principal.getName();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Forum> forumPage = forumRepo.findAll(pageable);
        List<ForumDto> forums = forumPage.getContent().stream()
                .map(this::convertToDto)
                .filter(s->s.isBookmarked()==true&(s.getCreatedBy().equals(userEmail)))
                .collect(Collectors.toList());
        return forums;
    }


    private ForumDto convertToDto(Forum forum) {
        return modelMapper.map(forum, ForumDto.class);
    }

}
