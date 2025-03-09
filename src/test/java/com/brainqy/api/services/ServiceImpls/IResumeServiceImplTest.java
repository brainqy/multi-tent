package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @since 09-03-2025
 * @project multi-tent
 */

import com.brainqy.api.domain.resume.Resume;
import com.brainqy.api.domain.resume.ResumeDto;
import com.brainqy.api.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IResumeServiceImplTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IResumeServiceImpl resumeService;

    private ResumeDto resumeDto;
    private Resume resume;
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        resumeDto = new ResumeDto();
        resumeDto.setApplicantName("John Doe");
        resumeDto.setEmail("john.doe@example.com");
        resumeDto.setPhone("1234567890");
        resumeDto.setAddress("123 Main St");
        resumeDto.setCity("Anytown");
        resumeDto.setState("Anystate");
        resumeDto.setCountry("Anycountry");
        resumeDto.setPostalCode("12345");
        resumeDto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        resumeDto.setCertifications(List.of("Certification 1"));
        resumeDto.setAchievements(List.of("Achievement 1"));
        resumeDto.setLinkedInUrl("http://linkedin.com/in/johndoe");
        resumeDto.setGithubUrl("http://github.com/johndoe");
        resumeDto.setPortfolioUrl("http://johndoe.com");
        resumeDto.setReferenceName("Jane Doe");
        resumeDto.setReferenceEmail("jane.doe@example.com");
        resumeDto.setReferencePhone("0987654321");

        resume = new Resume();
        resume.setId(1L);
        resume.setApplicantName("John Doe");
        resume.setEmail("john.doe@example.com");
        resume.setPhone("1234567890");
        resume.setAddress("123 Main St");
        resume.setCity("Anytown");
        resume.setState("Anystate");
        resume.setCountry("Anycountry");
        resume.setPostalCode("12345");
        resume.setDateOfBirth(LocalDate.of(1990, 1, 1));
        resume.setCertifications(List.of("Certification 1"));
        resume.setAchievements(List.of("Achievement 1"));
        resume.setLinkedInUrl("http://linkedin.com/in/johndoe");
        resume.setGithubUrl("http://github.com/johndoe");
        resume.setPortfolioUrl("http://johndoe.com");
        resume.setReferenceName("Jane Doe");
        resume.setReferenceEmail("jane.doe@example.com");
        resume.setReferencePhone("0987654321");

        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("john.doe@example.com");
    }

    @Test
    void saveResume_ShouldReturnSavedResume() {
        when(modelMapper.map(any(ResumeDto.class), eq(Resume.class))).thenReturn(resume);
        when(resumeRepository.save(any())).thenReturn(resume);

        Resume savedResume = resumeService.saveResume(resumeDto, principal);

        assertNotNull(savedResume);
        assertEquals(resume.getId(), savedResume.getId());
        verify(resumeRepository, times(1)).save(any());
    }

    @Test
    void saveResume_ShouldHandleEmptyCertificationsAndAchievements() {
        resumeDto.setCertifications(Collections.emptyList());
        resumeDto.setAchievements(Collections.emptyList());

        when(modelMapper.map(any(ResumeDto.class), eq(Resume.class))).thenReturn(resume);
        when(resumeRepository.save(any())).thenReturn(resume);

        Resume savedResume = resumeService.saveResume(resumeDto, principal);

        assertNotNull(savedResume);
        assertTrue(savedResume.getCertifications().isEmpty());
        assertTrue(savedResume.getAchievements().isEmpty());
        verify(resumeRepository, times(1)).save(any());
    }

    @Test
    void saveAsStarred_ShouldReturnUpdatedResume() {
        when(resumeRepository.findById(1L)).thenReturn(Optional.of(resume));
        when(resumeRepository.save(any())).thenReturn(resume);
        when(modelMapper.map(any(), eq(ResumeDto.class))).thenReturn(resumeDto);

        ResumeDto updatedResume = resumeService.saveAsStarred(1L);

        assertNotNull(updatedResume);
        assertEquals(resumeDto.getApplicantName(), updatedResume.getApplicantName());
        verify(resumeRepository, times(1)).findById(1L);
        verify(resumeRepository, times(1)).save(any());
    }

    @Test
    void saveAsStarred_ShouldThrowExceptionWhenResumeNotFound() {
        when(resumeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> resumeService.saveAsStarred(1L));
        verify(resumeRepository, times(1)).findById(1L);
        verify(resumeRepository, never()).save(any());
    }

    @Test
    void getResumeById_ShouldReturnResume() {
        when(resumeRepository.findById(1L)).thenReturn(Optional.of(resume));

        Optional<Resume> foundResume = resumeService.getResumeById(1L);

        assertTrue(foundResume.isPresent());
        assertEquals(resume.getId(), foundResume.get().getId());
        verify(resumeRepository, times(1)).findById(1L);
    }

    @Test
    void getResumeById_ShouldReturnEmptyWhenResumeNotFound() {
        when(resumeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Resume> foundResume = resumeService.getResumeById(1L);

        assertFalse(foundResume.isPresent());
        verify(resumeRepository, times(1)).findById(1L);
    }

    @Test
    void getAllResumes_ShouldReturnListOfResumes() {
        when(resumeRepository.getAllResumesByEmail("john.doe@example.com")).thenReturn(List.of(resume));

        List<Resume> resumes = resumeService.getAllResumes(principal);

        assertNotNull(resumes);
        assertFalse(resumes.isEmpty());
        assertEquals(1, resumes.size());
        verify(resumeRepository, times(1)).getAllResumesByEmail("john.doe@example.com");
    }

    @Test
    void getAllResumes_ShouldReturnEmptyListWhenNoResumes() {
        when(resumeRepository.getAllResumesByEmail("john.doe@example.com")).thenReturn(Collections.emptyList());

        List<Resume> resumes = resumeService.getAllResumes(principal);

        assertNotNull(resumes);
        assertTrue(resumes.isEmpty());
        verify(resumeRepository, times(1)).getAllResumesByEmail("john.doe@example.com");
    }

    @Test
    void deleteResume_ShouldDeleteResume() {
        doNothing().when(resumeRepository).deleteById(1L);

        resumeService.deleteResume(1L);

        verify(resumeRepository, times(1)).deleteById(1L);
    }
}
