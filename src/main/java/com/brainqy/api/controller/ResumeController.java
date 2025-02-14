package com.brainqy.api.controller;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-09-2024
 */
import com.brainqy.api.domain.resume.Resume;
import com.brainqy.api.domain.resume.ResumeDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.services.IServices.JobService;
import com.brainqy.api.services.IServices.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private JobService jobService;

    // Create a new resume

        @PostMapping
        public ResponseEntity<Resume> createResume(@RequestBody ResumeDto resumeDTO, Principal principal) {
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
