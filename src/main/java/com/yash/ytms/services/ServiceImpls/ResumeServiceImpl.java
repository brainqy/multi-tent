package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.atsscan.SectionDataWrapper;
import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;
import com.yash.ytms.domain.resume.Experience;
import com.yash.ytms.domain.resume.Qualification;
import com.yash.ytms.domain.resume.Resume;
import com.yash.ytms.domain.resume.ResumeDto;
import com.yash.ytms.repository.ResumeRepository;
import com.yash.ytms.security.jwt.JwtAuthenticationFilter;
import com.yash.ytms.services.IServices.ResumeService;
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
public class ResumeServiceImpl implements ResumeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResumeServiceImpl.class);

    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    @Transactional
    @CachePut(value = "resumeCache", key = "#resume.getId()")
    public Resume saveResume(ResumeDto resumeDTO,Principal principal) {
        String userEmail=principal.getName();
        // Convert ResumeDTO to Resume entity
        Resume resume = new Resume();
        resume.setCreatedBy(userEmail);
        resume.setApplicantName(resumeDTO.getApplicantName());
        resume.setEmail(resumeDTO.getEmail());
        resume.setPhone(resumeDTO.getPhone());
        resume.setAddress(resumeDTO.getAddress());
        resume.setCity(resumeDTO.getCity());
        resume.setState(resumeDTO.getState());
        resume.setCountry(resumeDTO.getCountry());
        resume.setPostalCode(resumeDTO.getPostalCode());
        resume.setDateOfBirth(resumeDTO.getDateOfBirth());

        resume.setCertifications(resumeDTO.getCertifications());
        resume.setAchievements(resumeDTO.getAchievements());
        resume.setLinkedInUrl(resumeDTO.getLinkedInUrl());
        resume.setGithubUrl(resumeDTO.getGithubUrl());
        resume.setPortfolioUrl(resumeDTO.getPortfolioUrl());
        resume.setReferenceName(resumeDTO.getReferenceName());
        resume.setReferenceEmail(resumeDTO.getReferenceEmail());
        resume.setReferencePhone(resumeDTO.getReferencePhone());
        // fix this Save the resume

        // Convert experiences and qualifications
        List<Experience> experiences = resumeDTO.getExperiences().stream()
                .map(exp -> {
                    Experience experience = new Experience();
                    experience.setJobTitle(exp.getJobTitle());
                    experience.setCompanyName(exp.getCompanyName());
                    experience.setStartDate(exp.getStartDate());
                    experience.setEndDate(exp.getEndDate());
                    experience.setResume(resume);
                    return experience;
                })
                .collect(Collectors.toList());

        List<Qualification> qualifications = resumeDTO.getQualifications().stream()
                .map(qual -> {
                    Qualification qualification = new Qualification();
                    qualification.setDegree(qual.getDegree());
                    qualification.setInstitution(qual.getInstitution());
                    qualification.setResume(resume);
                    return qualification;
                })
                .collect(Collectors.toList());

        resume.setExperiences(experiences);
        resume.setQualifications(qualifications);


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
