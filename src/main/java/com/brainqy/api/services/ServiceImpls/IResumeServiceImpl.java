package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.resume.Experience;
import com.brainqy.api.domain.resume.Qualification;
import com.brainqy.api.domain.resume.Resume;
import com.brainqy.api.domain.resume.ResumeDto;
import com.brainqy.api.repository.ResumeRepository;
import com.brainqy.api.services.IServices.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-09-2024
 */
@Service
public class IResumeServiceImpl implements ResumeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IResumeServiceImpl.class);

    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    @CachePut(value = "resumeCache", key = "#resume.getId()")
    public Resume saveResume(ResumeDto resumeDto, Principal principal) {
        Resume resume = modelMapper.map(resumeDto, Resume.class);
        if (resume == null) {
            throw new IllegalArgumentException("Mapping from ResumeDto to Resume failed.");
        }
        resume.setEmail(principal.getName());
        resume.setCertifications(resumeDto.getCertifications() != null ? resumeDto.getCertifications() : Collections.emptyList());
        resume.setAchievements(resumeDto.getAchievements() != null ? resumeDto.getAchievements() : Collections.emptyList());
        return resumeRepository.save(resume);
    }
    @Override
    @CachePut(value = "resumeCache", key = "#id")
    public ResumeDto saveAsStarred(Long id) {
        Optional<Resume> optionalEntity = resumeRepository.findById(id);
        if (optionalEntity.isPresent()) {
            Resume entity = optionalEntity.get();
            if(entity.isStarred()){
                entity.setStarred(false);
            }else {
                entity.setStarred(true);
            }
            resumeRepository.save(entity);
            ResumeDto entityDto = modelMapper.map(entity, ResumeDto.class);
            return entityDto;
        }
        throw new EntityNotFoundException("SectionDataWrapperDto not found with id: " + id);

    }

    @Override
    @Cacheable(value = "resumeCache", key = "#id")
    public Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }
    @Override
    @Cacheable(value = "resumeCache", key = "#principal.getName()")
    public List<Resume> getAllResumes(Principal principal) {
        String userEmail=principal.getName();

        return resumeRepository.getAllResumesByEmail(userEmail);
    }
    @Override
    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
        evictResumeFromCache(id);
    }
    @CacheEvict(value = "resumeCache", key = "#id")
    public void evictResumeFromCache(Long id) {
        // This will clear the cache for the specific resume ID
    }

}
