package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.resume.Resume;
import com.yash.ytms.domain.resume.ResumeDto;

import java.security.Principal;
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

    public Optional<Resume> getResumeById(Long id);

    public List<Resume> getAllResumes(Principal principal);

    public void deleteResume(Long id);

    Resume saveResume(ResumeDto resumeDTO, Principal principal);
    ResumeDto saveAsStarred(Long id);
}
