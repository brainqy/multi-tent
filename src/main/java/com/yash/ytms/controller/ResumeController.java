package com.yash.ytms.controller;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-09-2024
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.ytms.domain.Job;
import com.yash.ytms.domain.resume.Experience;
import com.yash.ytms.domain.resume.Qualification;
import com.yash.ytms.domain.resume.Resume;
import com.yash.ytms.domain.resume.ResumeDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.services.IServices.JobService;
import com.yash.ytms.services.IServices.ResumeService;
import groovyjarjarasm.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private JobService jobService;

    // Create a new resume
    @PostMapping("/create-resume")
    public ResponseEntity<Resume> createResumeWithFile(
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestParam("jobOpportunityId") Long jobOpportunityId) throws IOException {

        byte[] resumeBytes = resumeFile.getBytes();
        Resume resume = new Resume();
   //     resume.setBase(base);
        //resume.setResume(resumeBytes);
        // Assuming you have a service to get JobOpportunity by ID
        Job jobOpportunity = jobService.findById(jobOpportunityId);

        resume.setJob(jobOpportunity);

        Resume savedResume = resumeService.saveResume(resume);
        return ResponseEntity.ok(savedResume);
    }


        @PostMapping
        public ResponseEntity<Resume> createResume(@RequestBody ResumeDto resumeDTO) {
            // Convert ResumeDTO to Resume entity
            Resume resume = new Resume();
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
            Job job = new Job();
            job.setId(12l);
            resume.setJob(job);
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

            Resume savedResume = resumeService.saveResume(resume);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResume);
        }


    // Get a resume by ID
    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        Resume resume = resumeService.getResumeById(id)
                .orElseThrow(() -> new ApplicationException("Resume not found"));
        return ResponseEntity.ok(resume);
    }

    // Get all resumes
    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        List<Resume> resumes = resumeService.getAllResumes();
        return ResponseEntity.ok(resumes);
    }

    // Delete a resume
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }

    // Additional endpoints (e.g., update resume) if needed
}
