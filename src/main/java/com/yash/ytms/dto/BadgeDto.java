package com.yash.ytms.dto;

import com.yash.ytms.constants.BadgeRule;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 14-09-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeDto {
    private String name;
    private String description;
    private String icon; // New icon field
    private String backgroundColor; // New background color field
    private BadgeRule rule;  // Enum for badge rules
    private int threshold;
}
