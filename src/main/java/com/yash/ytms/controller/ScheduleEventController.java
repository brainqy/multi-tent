package com.yash.ytms.controller;

import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.ScheduleEventDto;
import com.yash.ytms.services.IServices.IScheduleEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/calendar/events")
public class ScheduleEventController {

    @Autowired
    private IScheduleEventService scheduleEventService;

    @GetMapping("/get/all")
    public ResponseEntity<List<ScheduleEventDto>> getAllScheduleEvents() {
        List<ScheduleEventDto> scheduleEvents = this.scheduleEventService.getAllScheduleEvents();
        return new ResponseEntity<>(scheduleEvents, HttpStatus.OK);
    }

    @GetMapping("/get/{eventId}")
    public ResponseEntity<ScheduleEventDto> getScheduleEventById(@PathVariable Integer eventId) {
        ScheduleEventDto scheduleEvent = this.scheduleEventService.getScheduleEventById(eventId);
        return new ResponseEntity<>(scheduleEvent, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ScheduleEventDto> createScheduleEvent(@RequestBody ScheduleEventDto scheduleEventDto,
                                                                Principal principal) {
        ScheduleEventDto scheduleEvent = this.scheduleEventService.createScheduleEvent(scheduleEventDto, principal);
        return new ResponseEntity<>(scheduleEvent, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<ResponseWrapperDto> deleteScheduleEventById(@PathVariable Integer eventId,
                                                                      Principal principal) {
        ResponseWrapperDto responseWrapperDto = this.scheduleEventService.deleteScheduleEventById(eventId, principal);
        return new ResponseEntity<>(responseWrapperDto, HttpStatus.OK);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<ResponseWrapperDto> updateScheduleEvent(@PathVariable Integer eventId,
                                                                  @RequestBody ScheduleEventDto scheduleEventDto,
                                                                  Principal principal) {
        ResponseWrapperDto responseWrapperDto = this.scheduleEventService.updateScheduleEvent(eventId, scheduleEventDto, principal);
        return new ResponseEntity<>(responseWrapperDto, HttpStatus.OK);
    }

    @GetMapping("/get/trainer-calendar")
    public ResponseEntity<ResponseWrapperDto> searchByTrainerEmail(@RequestParam("email") String trainerEmail) {

        ResponseWrapperDto events = scheduleEventService.searchByTrainer(trainerEmail);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

}
