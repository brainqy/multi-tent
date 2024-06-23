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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int section_id;
    private String section;
    private double percentage;
    private int issues;

     @OneToMany(cascade = CascadeType.ALL, targetEntity = DataItem.class)
    @JoinColumn(name="section_id",referencedColumnName = "section_id")
    private List<DataItem> data;

}
