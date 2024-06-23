package com.yash.ytms.controller;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 29-05-2024
 */
import com.yash.ytms.domain.Job;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.services.IServices.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.findById(id)
                .map(job -> ResponseEntity.ok().body(job))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        jobService.save(job);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        return jobService.findById(id)
                .map(job -> {
                    job.setJobRole(jobDetails.getJobRole());
                    job.setJobLocation(jobDetails.getJobLocation());
                    job.setCompany(jobDetails.getCompany());
                    job.setStatus(jobDetails.getStatus());
                    return ResponseEntity.ok().body(jobService.save(job));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Job> updateJobStatus(@PathVariable Long id, @RequestBody Job jobDetails) {
        Job job = jobService.findById(id)
                .orElseThrow(() -> new ApplicationException("Job not found with id " + id));
        job.setStatus(jobDetails.getStatus());
        job.setJobLocation(jobDetails.getJobLocation());
        job.setJobRole(jobDetails.getJobRole());
        job.setDateSpecified(jobDetails.getDateSpecified());
        job.setCompany(jobDetails.getCompany());
        job.setJobListingUrl(jobDetails.getJobListingUrl());
        job.setJobDescription(jobDetails.getJobDescription());
        job.setSalary(jobDetails.getSalary());
        Job updatedJob = jobService.save(job);
        return ResponseEntity.ok(updatedJob);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteJob(@PathVariable Long id) {
        return jobService.findById(id)
                .map(job -> {
                    jobService.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<Job> getJobsByStatus(@PathVariable String status) {
        return jobService.findByStatus(status);
    }
}

