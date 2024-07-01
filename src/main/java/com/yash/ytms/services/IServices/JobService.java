package com.yash.ytms.services.IServices;

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
import com.yash.ytms.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface JobService {
    public ResponseWrapperDto save(Job job);
    public List<Job>  findAll();
    public Job findById(Long id);
    public ResponseWrapperDto deleteById(Long id);
    public List<Job> findByStatus(String status);


}

