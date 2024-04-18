package com.yash.ytms.domain.atsscan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

public class ContentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int content_id;
    private boolean tickIcon;
    private String contentValue;
    @ManyToOne
    @JoinColumn(name = "data_item_id")
    private DataItem dataItem;
}
