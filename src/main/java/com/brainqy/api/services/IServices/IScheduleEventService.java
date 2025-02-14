package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.ScheduleEventDto;

import java.security.Principal;
import java.util.List;


public interface IScheduleEventService {

    ScheduleEventDto createScheduleEvent(ScheduleEventDto scheduleEventDto, Principal principal);

    ResponseWrapperDto searchByTrainer(String trainerEmail);

    ScheduleEventDto getScheduleEventById(Integer eventId);

    List<ScheduleEventDto> getAllScheduleEvents();

    ResponseWrapperDto deleteScheduleEventById(Integer eventId, Principal principal);

    ResponseWrapperDto updateScheduleEvent(Integer eventId, ScheduleEventDto scheduleEventDto, Principal principal);

    List<ScheduleEventDto> getAllScheduleEventsExceptLoggedUser(Principal principal);

    List<ScheduleEventDto> getAllScheduleEventsForLoggedInUser(Principal principal);
}
