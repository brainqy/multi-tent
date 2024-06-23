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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int data_id;
    private String title;
    private boolean infoIcon;
    private  double weight;


    @ManyToOne
    @JoinColumn(name = "section_id")
    private SectionData sectionData;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = ContentItem.class)
    @JoinColumn(name="data_id",referencedColumnName = "data_id")
    private List<ContentItem> content;
}
