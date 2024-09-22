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
import com.yash.ytms.domain.atsscan.SectionDataWrapperDto;
import com.yash.ytms.domain.resume.Experience;
import com.yash.ytms.domain.resume.Qualification;
import com.yash.ytms.domain.resume.Resume;
import com.yash.ytms.domain.resume.ResumeDto;
import com.yash.ytms.dto.ResponseWrapperDto;
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
import java.security.Principal;
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

        @PostMapping
        public ResponseEntity<Resume> createResume(@RequestBody ResumeDto resumeDTO,Principal principal) {
            Resume savedResume = resumeService.saveResume(resumeDTO,principal);
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
    @GetMapping("/get-all-resumes")
    public ResponseEntity<List<Resume>> getAllResumes(Principal principal) {
        List<Resume> resumes = resumeService.getAllResumes(principal);
        return ResponseEntity.ok(resumes);
    }

    // Delete a resume
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }

    // Additional endpoints (e.g., update resume) if needed
    @PutMapping("/{id}/star")
    public ResponseEntity<ResumeDto> saveAsStarred(@PathVariable Long id) {
        ResumeDto updatedEntity = this.resumeService.saveAsStarred(id);
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        wrapperDto.setStatus("SUCCESS");
        wrapperDto.setData(updatedEntity);
        return new ResponseEntity(wrapperDto,HttpStatus.OK);
    }
}
