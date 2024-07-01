package com.yash.ytms.controller;

import com.yash.ytms.domain.InterviewSlot;
import com.yash.ytms.dto.InterviewPageDto;
import com.yash.ytms.dto.InterviewSlotDto;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.services.IServices.InterviewSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseWrapperDto> saveInterviewSlot(@RequestBody InterviewSlotDto interviewSlot, Principal principal) {
        ResponseWrapperDto interviewSlotWrapper = interviewSlotService.saveInterviewSlot(interviewSlot,principal);
        return  new ResponseEntity(interviewSlot,HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity getAllInterviewSlots() {
        ResponseWrapperDto responseWrapperDto= new ResponseWrapperDto();
        InterviewPageDto allInterviewSlots = interviewSlotService.getAllInterviewSlots();
        responseWrapperDto.setData(allInterviewSlots);
        responseWrapperDto.setStatus("SUCCESS");
        responseWrapperDto.setMessage("Data retrieved successfully");
        return  new ResponseEntity(responseWrapperDto, HttpStatus.OK);

    }
    @PutMapping("/{id}")
    public ResponseEntity<InterviewSlotDto> updateInterviewSlot(@PathVariable Long id, @RequestBody InterviewSlotDto updatedSlotDto) {
        InterviewSlotDto updatedSlot = interviewSlotService.updateInterviewSlot(id, updatedSlotDto);
        return ResponseEntity.ok(updatedSlot);
    }
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<InterviewSlotDto> cancelInterviewSlot(@PathVariable Long id,Principal principal) {
        InterviewSlotDto canceledSlot = interviewSlotService.cancelInterviewSlot(id,principal);
        return ResponseEntity.ok(canceledSlot);
    }

}