package com.yash.ytms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 28-02-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IcsRequest {
    private String eventName;
    private String organizerName;
    private String organizerEmail;
    private List<String> attendees;
    private String subject;
    private String description;
    private String location;
    private Date startTime;
    private Date endTime;

    // Constructors, getters, and setters
}