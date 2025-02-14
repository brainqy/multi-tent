package com.brainqy.api.domain;

import com.brainqy.api.domain.resume.Resume;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 29-05-2024
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobRole;
    private String jobLocation;
    private String jobDescription;
    private String company;
    private String status;
    private String jobListingUrl;
    private Double salary;
    private String dateSpecified;
    private String createdBy;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes;

}
