package com.yash.ytms.domain;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
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
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String day;
    @NotNull
    private String slot;
    @NotNull
    private String interviewType;
    private String kindOfInterviewType;
    private String status;
    @ManyToOne
    @JoinColumn(name = "createdBy")
    private YtmsUser scheduleUser;

    // Getters and setters
}