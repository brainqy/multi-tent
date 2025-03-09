package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import com.brainqy.api.domain.Job;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.repository.JobRepository;
import com.brainqy.api.services.ServiceImpls.IJobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IJobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private IJobServiceImpl jobService;

    private Job job;
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        job = new Job();
        job.setId(1L);
        job.setJobRole("Developer");
        job.setJobLocation("Remote");
        job.setJobDescription("Job Description");
        job.setCompany("Company");
        job.setStatus("Open");
        job.setJobListingUrl("http://example.com");
        job.setSalary(100000.0);
        job.setDateSpecified("2024-01-01");
        job.setCreatedBy("user@example.com");

        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@example.com");
    }

    @Test
    void findAll_ShouldReturnListOfJobs() {
        when(jobRepository.findAll()).thenReturn(Arrays.asList(job));

        List<Job> jobs = jobService.findAll();

        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());
        assertEquals(1, jobs.size());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnJob() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        Job foundJob = jobService.findById(1L);

        assertNotNull(foundJob);
        assertEquals(job.getId(), foundJob.getId());
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowExceptionWhenJobNotFound() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> jobService.findById(1L));
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedJob() {
        when(jobRepository.save(job)).thenReturn(job);

        ResponseWrapperDto response = jobService.save(job, principal);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Job saved successfully", response.getMessage());
        assertEquals(job, response.getData());
        verify(jobRepository, times(1)).save(job);
    }

    @Test
    void deleteById_ShouldReturnSuccessWhenJobExists() {
        when(jobRepository.existsById(1L)).thenReturn(true);

        ResponseWrapperDto response = jobService.deleteById(1L);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Job deleted successfully", response.getMessage());
        verify(jobRepository, times(1)).existsById(1L);
        verify(jobRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldReturnErrorWhenJobNotFound() {
        when(jobRepository.existsById(1L)).thenReturn(false);

        ResponseWrapperDto response = jobService.deleteById(1L);

        assertNotNull(response);
        assertEquals("ERROR", response.getStatus());
        assertEquals("Job not found", response.getMessage());
        verify(jobRepository, times(1)).existsById(1L);
        verify(jobRepository, never()).deleteById(1L);
    }

    @Test
    void findByStatus_ShouldReturnListOfJobs() {
        when(jobRepository.findAllByStatus("Open")).thenReturn(Arrays.asList(job));

        List<Job> jobs = jobService.findByStatus("Open");

        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());
        assertEquals(1, jobs.size());
        verify(jobRepository, times(1)).findAllByStatus("Open");
    }

    @Test
    void getJobsByEmail_ShouldReturnListOfJobs() {
        when(jobRepository.getJobsByEmail("user@example.com")).thenReturn(Arrays.asList(job));

        List<Job> jobs = jobService.getJobsByEmail("user@example.com");

        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());
        assertEquals(1, jobs.size());
        verify(jobRepository, times(1)).getJobsByEmail("user@example.com");
    }
}
