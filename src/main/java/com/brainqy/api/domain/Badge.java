package com.brainqy.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.brainqy.api.constants.BadgeRule;
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
 * @since 14-09-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String icon; // New icon field
    private String backgroundColor; // New background color field
    @Enumerated(EnumType.STRING)
    private BadgeRule rule;  // Enum for badge rules
    private int threshold;
    @ManyToMany(mappedBy = "earnedBadges") // Bidirectional relationship with YtmsUser
    @JsonIgnore // Prevent circular reference during serialization
    private List<YtmsUser> users;
}
