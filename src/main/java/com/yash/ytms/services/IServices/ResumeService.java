package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.resume.Resume;

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
public interface ResumeService {
    public Resume saveResume(Resume resume);

    public Optional<Resume> getResumeById(Long id);

    public List<Resume> getAllResumes();

    public void deleteResume(Long id);

}
