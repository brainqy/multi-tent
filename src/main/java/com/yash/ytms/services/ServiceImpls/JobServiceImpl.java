package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.Job;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.repository.JobRepository;
import com.yash.ytms.services.IServices.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 01-07-2024
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<Job> findAll() {
        List<Job> jobs = jobRepository.findAll();
return  jobs;
    }

    @Override
    public Job findById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return  job.get();
    }

    @Override
    public ResponseWrapperDto save(Job job) {
        Job savedJob = jobRepository.save(job);
        return createResponse("SUCCESS", "Job saved successfully", savedJob);
    }

    @Override
    public ResponseWrapperDto deleteById(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return createResponse("SUCCESS", "Job deleted successfully", null);
        } else {
            return createResponse("ERROR", "Job not found", null);
        }
    }

    @Override
    public List<Job> findByStatus(String status) {
        List<Job> jobs = jobRepository.findAllByStatus(status);
        return  jobs;
    }

    private ResponseWrapperDto createResponse(String status, String message, Object data) {
        ResponseWrapperDto response = new ResponseWrapperDto();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
