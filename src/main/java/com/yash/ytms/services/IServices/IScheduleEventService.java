package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.ScheduleEventDto;

import java.security.Principal;
import java.util.List;


public interface IScheduleEventService {

    ScheduleEventDto createScheduleEvent(ScheduleEventDto scheduleEventDto, Principal principal);

    ResponseWrapperDto searchByTrainer(String trainerEmail);

    ScheduleEventDto getScheduleEventById(Integer eventId);

    List<ScheduleEventDto> getAllScheduleEvents();

    ResponseWrapperDto deleteScheduleEventById(Integer eventId, Principal principal);

    ResponseWrapperDto updateScheduleEvent(Integer eventId, ScheduleEventDto scheduleEventDto, Principal principal);
}
