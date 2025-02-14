package com.brainqy.api.controller;

import com.brainqy.api.dto.InterviewPageDto;
import com.brainqy.api.dto.InterviewSlotDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.ScheduleEventDto;
import com.brainqy.api.services.IServices.IScheduleEventService;
import com.brainqy.api.services.IServices.InterviewSlotService;
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
    @Autowired
    private IScheduleEventService eventService;

    @PostMapping
    public ResponseEntity<ResponseWrapperDto> saveInterviewSlot(@RequestBody InterviewSlotDto interviewSlot, Principal principal) {
        ResponseWrapperDto interviewSlotWrapper = interviewSlotService.saveInterviewSlot(interviewSlot,principal);
        return  new ResponseEntity(interviewSlot,HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity getAllInterviewSlots(Principal principal) {
        ResponseWrapperDto responseWrapperDto= new ResponseWrapperDto();
        InterviewPageDto allInterviewSlots = interviewSlotService.getAllInterviewSlots(principal);
        responseWrapperDto.setData(allInterviewSlots);
        responseWrapperDto.setStatus("SUCCESS");
        responseWrapperDto.setMessage("Data retrieved successfully");
        return  new ResponseEntity(responseWrapperDto, HttpStatus.OK);
    }
    @GetMapping("/interview-slots-except-loggedIn")
    public  ResponseEntity getAllInterviewSlotsExceptLoggedInUser(Principal principal) {
        ResponseWrapperDto responseWrapperDto= new ResponseWrapperDto();
        InterviewPageDto allInterviewSlots = interviewSlotService.getAllInterviewSlotsExceptLoggedInUser(principal);
        responseWrapperDto.setData(allInterviewSlots);
        responseWrapperDto.setStatus("SUCCESS");
        responseWrapperDto.setMessage("Data retrieved successfully");
        return  new ResponseEntity(responseWrapperDto, HttpStatus.OK);

    }
    @GetMapping("/jobId/{jobId}")
    public  ResponseEntity getAllInterviewSlots( @PathVariable long jobId, Principal principal) {
        ResponseWrapperDto responseWrapperDto= new ResponseWrapperDto();
        List<InterviewSlotDto> allInterviewSlots = interviewSlotService.getAllInterviewSlotsByJobId(jobId,principal);
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
    @GetMapping("/get_available_slots")
    public  ResponseEntity<List<ScheduleEventDto>> getAvailableInterviewSlots(){
        List<ScheduleEventDto> allEvents = eventService.getAllScheduleEvents();
        return  ResponseEntity.ok(allEvents);
    }
        

}