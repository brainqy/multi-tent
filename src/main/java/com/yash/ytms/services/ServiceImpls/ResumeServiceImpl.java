package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.resume.Resume;
import com.yash.ytms.repository.ResumeRepository;
import com.yash.ytms.services.IServices.ResumeService;
import jakarta.transaction.Transactional;
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
 * @since 22-09-2024
 */
@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;
    @Override
    @Transactional
    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }
    @Override
    public Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }
    @Override
    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }
    @Override
    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }

}
