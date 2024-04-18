package com.yash.ytms.domain.atsscan;

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
 * @since 15-04-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class SectionData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int section_id;
    private String section;
    private double percentage;
    private int issues;

    @ManyToOne
    @JoinColumn(name = "section_data_wrapper_id")
    private SectionDataWrapper sectionDataWrapper;

    @OneToMany(mappedBy = "sectionData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataItem> data;

}
