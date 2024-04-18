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

public class DataItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int data_id;
    private String title;
    private boolean infoIcon;
    private  double weight;


    @OneToMany(mappedBy = "dataItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContentItem> content;
    @ManyToOne
    @JoinColumn(name = "section_data_id")
    private SectionData sectionData;
}
