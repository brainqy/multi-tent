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
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.services.IServices.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public List<Job>  getAllJobs() {
        List<Job>  jobs = jobService.findAll();
         return  jobs;

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapperDto> getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        ResponseWrapperDto wrapperDto = new ResponseWrapperDto();
        wrapperDto.setData(job);
        wrapperDto.setStatus("SUCCESS");
        return  new ResponseEntity<>(wrapperDto,HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        jobService.save(job);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Optional<Job> jobOptional = Optional.ofNullable(jobService.findById(id));
        Job job= jobOptional.get();
                    job.setJobRole(jobDetails.getJobRole());
                    job.setJobLocation(jobDetails.getJobLocation());
                    job.setCompany(jobDetails.getCompany());
                    job.setStatus(jobDetails.getStatus());
                     jobService.save(job);
                     return new ResponseEntity<>(HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseWrapperDto> updateJobStatus(@PathVariable Long id, @RequestBody Job jobDetails) {
        Job job = Optional.ofNullable(jobService.findById(id)).get();
        job.setStatus(jobDetails.getStatus());
        job.setJobLocation(jobDetails.getJobLocation());
        job.setJobRole(jobDetails.getJobRole());
        job.setDateSpecified(jobDetails.getDateSpecified());
        job.setCompany(jobDetails.getCompany());
        job.setJobListingUrl(jobDetails.getJobListingUrl());
        job.setJobDescription(jobDetails.getJobDescription());
        job.setSalary(jobDetails.getSalary());
        ResponseWrapperDto updatedJob = jobService.save(job);
        return new ResponseEntity(updatedJob, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapperDto> deleteJob(@PathVariable Long id) {
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        Job job = jobService.findById(id);
        if(job!=null){
            jobService.deleteById(id);
        }
                wrapperDto.setStatus("SUCCESS");
        wrapperDto.setMessage("Deleted job successfully");
        return new ResponseEntity<>(wrapperDto,HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity getJobsByStatus(@PathVariable String status) {
        List<Job> jobs = jobService.findByStatus(status);
        ResponseWrapperDto wrapperDto = new ResponseWrapperDto();
        wrapperDto.setData(jobs);
        wrapperDto.setStatus("SUCCESS");
        return  new ResponseEntity<>(wrapperDto,HttpStatus.OK);


    }
}

