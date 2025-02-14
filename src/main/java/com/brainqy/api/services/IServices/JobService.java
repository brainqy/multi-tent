package com.brainqy.api.services.IServices;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 29-05-2024
 */

import com.brainqy.api.domain.Job;
import com.brainqy.api.dto.ResponseWrapperDto;

import java.security.Principal;
import java.util.List;


public interface JobService {
    public ResponseWrapperDto save(Job job, Principal principal);
    public List<Job>  findAll();
    public Job findById(Long id);
    public ResponseWrapperDto deleteById(Long id);
    public List<Job> findByStatus(String status);


    List<Job> getJobsByEmail(String userEmail);
}

