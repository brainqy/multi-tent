package com.yash.ytms.controller;

import com.yash.ytms.domain.InterviewSlot;
import com.yash.ytms.dto.InterviewSlotDto;
import com.yash.ytms.services.IServices.InterviewSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-04-2024
 */
@RestController
@RequestMapping("/interview-slots")
public class InterviewSlotController {
    @Autowired
    private InterviewSlotService interviewSlotService;

    @PostMapping
    public InterviewSlotDto saveInterviewSlot(@RequestBody InterviewSlotDto interviewSlot, Principal principal) {
        return interviewSlotService.saveInterviewSlot(interviewSlot,principal);
    }

    @GetMapping
    public List<InterviewSlotDto> getAllInterviewSlots() {
        return interviewSlotService.getAllInterviewSlots();
    }
    @PutMapping("/{id}")
    public ResponseEntity<InterviewSlotDto> updateInterviewSlot(@PathVariable Long id, @RequestBody InterviewSlotDto updatedSlotDto) {
        InterviewSlotDto updatedSlot = interviewSlotService.updateInterviewSlot(id, updatedSlotDto);
        return ResponseEntity.ok(updatedSlot);
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<InterviewSlotDto> cancelInterviewSlot(@PathVariable Long id) {
        InterviewSlotDto canceledSlot = interviewSlotService.cancelInterviewSlot(id);
        return ResponseEntity.ok(canceledSlot);
    }

}