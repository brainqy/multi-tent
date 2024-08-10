package com.yash.ytms.dto;

import com.yash.ytms.domain.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-04-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewSlotDto {
    private int id;
    private String day;
    private String slot;
    private String interviewType;
    private String kindOfInterviewType;
    private String status;
    private String meetingLink;
    private String hrName;
    private String mobileNumber;
    private  String description;
    private String hrEmail;
    private long jobId;
}
