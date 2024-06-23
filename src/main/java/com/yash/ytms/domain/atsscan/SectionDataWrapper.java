package com.yash.ytms.domain.atsscan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 15-04-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class SectionDataWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wrapper_id;
    @OneToMany(targetEntity= SectionData.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wrapper_id",referencedColumnName = "wrapper_id")
    private List<SectionData> allData;
    private double finalProgress;
    private String jobTitle;
    private LocalDateTime createdAt;
    private  String createdBy;
}
