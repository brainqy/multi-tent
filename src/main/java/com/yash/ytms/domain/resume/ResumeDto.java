package com.yash.ytms.domain.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-09-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    private String applicantName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private LocalDate dateOfBirth;
    private List<Experience> experiences = new ArrayList<>();
    private List<Qualification> qualifications = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();  // Initialize as empty list
    private List<String> achievements = new ArrayList<>();     // Initialize as empty list
    private String linkedInUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String referenceName;
    private String referenceEmail;
    private String referencePhone;
    private  boolean isStarred;
    private String createdBy;
}

