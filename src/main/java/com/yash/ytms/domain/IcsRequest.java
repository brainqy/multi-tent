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
    private String title; // Changed from eventName to title
    private String organizerName;
    private String organizerEmail;
    private List<String> attendees; // Changed from String to List<String>
    private String description;
    private String location;
    private Date startTime;
    private Date endTime;
    private String primaryColor; // Added primaryColor field
    // Constructors, getters, and setters
}