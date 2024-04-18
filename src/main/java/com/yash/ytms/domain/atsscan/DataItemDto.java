package com.yash.ytms.domain.atsscan;

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

public class DataItemDto {
    private String title;
    private boolean infoIcon;
    private List<ContentItemDto> content;
    private  double weight;

}
