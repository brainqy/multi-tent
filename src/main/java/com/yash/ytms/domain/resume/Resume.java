package com.yash.ytms.domain.resume;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yash.ytms.domain.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-09-2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean base;
/*    @Lob
    @Column(columnDefinition = "LONGBLOB") // Ensure larger file size is supported
    private byte[] resume;*/
    @ManyToOne(fetch = FetchType.LAZY) // Many resumes can be associated with one job
    @JoinColumn(name = "job_id", nullable = true) // job_id can be null
    private Job job;

    // Applicant's basic details
    private String applicantName;
    private String email;
    private String phone;

    // Personal information
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private LocalDate dateOfBirth;

    // Work experiences (multiple experiences for each resume)
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To handle parent-child relationship
    private List<Experience> experiences;

    // Qualifications (multiple degrees or qualifications)
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // To handle parent-child relationship
    private List<Qualification> qualifications;

    // Certifications and achievements
    @ElementCollection
    private List<String> certifications;  // Professional certifications
    @ElementCollection
    private List<String> achievements;    // Achievements or awards

    // Social links
    private String linkedInUrl;
    private String githubUrl;
    private String portfolioUrl;

    // References
    private String referenceName;
    private String referenceEmail;
    private String referencePhone;

    private  boolean isStarred;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime lastModified;
    private  String createdBy;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}
