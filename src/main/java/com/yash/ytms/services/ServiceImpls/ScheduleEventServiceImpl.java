package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.constants.RequestStatusTypes;
import com.yash.ytms.domain.ScheduleEvent;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.ScheduleEventDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.ScheduleEventRepository;
import com.yash.ytms.services.IServices.IScheduleEventService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleEventServiceImpl implements IScheduleEventService {

    @Autowired
    private ScheduleEventRepository scheduleEventRepository;

    @Autowired
    private IYtmsUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public ScheduleEventDto createScheduleEvent(ScheduleEventDto scheduleEventDto, Principal principal) {
        final String userName = principal.getName();
        YtmsUserDto userDto = this.userService.getUserByEmailAdd(userName);
        if (ObjectUtils.isNotEmpty(scheduleEventDto)
                && ObjectUtils.isNotEmpty(userDto)) {
            YtmsUser user = this.modelMapper.map(userDto, YtmsUser.class);
            ScheduleEvent scheduleEvent = this.modelMapper.map(scheduleEventDto, ScheduleEvent.class);
            scheduleEvent.setScheduleUser(user);

            //reassigned with newly created event details
            scheduleEvent = scheduleEventRepository.save(scheduleEvent);
            scheduleEventDto = this.modelMapper.map(scheduleEvent, ScheduleEventDto.class);
            scheduleEventDto.setScheduleUser(userDto);

            return scheduleEventDto;
        } else
            throw new ApplicationException("Event details is invalid, please try again !");
    }

    @Override
    public ResponseWrapperDto searchByTrainer(String trainerEmail) {
        if (StringUtils.isNotEmpty(trainerEmail)) {
            ResponseWrapperDto responseWrapperDto = new ResponseWrapperDto();
            List<ScheduleEvent> scheduleEvents = this.scheduleEventRepository.findAllEventsByTrainerEmail(trainerEmail);

            if (scheduleEvents.isEmpty()) {
                responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
                responseWrapperDto.setMessage("No Events Found for this trainer");
                responseWrapperDto.setData("Nil");
            } else {
                List<ScheduleEventDto> scheduleEventsDto = scheduleEvents
                        .stream()
                        .map(e -> this.modelMapper.map(e, ScheduleEventDto.class))
                        .toList();
                responseWrapperDto.setStatus(RequestStatusTypes.SUCCESS.toString());
                responseWrapperDto.setMessage("Events found for trainer");
                responseWrapperDto.setData(scheduleEventsDto);
            }
            return responseWrapperDto;
        } else
            throw new ApplicationException("Trainer email is empty or null, please check & try again.");
    }

    @Override
    public ScheduleEventDto getScheduleEventById(Integer eventId) {
        if (ObjectUtils.isNotEmpty(eventId)) {
            Optional<ScheduleEvent> optionalEvent = scheduleEventRepository.findById(eventId);
            if (optionalEvent.isPresent()) {
                ScheduleEvent scheduleEvent = optionalEvent.get();
                return this.modelMapper.map(scheduleEvent, ScheduleEventDto.class);
            } else
                throw new ApplicationException("Event not found !");
        } else
            throw new ApplicationException("Event id is null or empty, please check & try again !");
    }

    @Override
    public List<ScheduleEventDto> getAllScheduleEvents() {
        List<ScheduleEvent> scheduleEvents = scheduleEventRepository.findAll();
        if (!scheduleEvents.isEmpty()) {
            return scheduleEvents
                    .stream()
                    .map(se -> this
                            .modelMapper
                            .map(se, ScheduleEventDto.class))
                    .toList();
        } else
            throw new ApplicationException("No Events found !");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public ResponseWrapperDto deleteScheduleEventById(Integer eventId, Principal principal) {
        if (ObjectUtils.isNotEmpty(eventId)) {
            ResponseWrapperDto responseWrapperDto = new ResponseWrapperDto();
            Optional<ScheduleEvent> scheduleEventOptional = this.scheduleEventRepository.findById(eventId);
            if (scheduleEventOptional.isPresent()) {
                ScheduleEvent scheduleEvent = scheduleEventOptional.get();
                final String userName = principal.getName();
                YtmsUserDto userDto = this.userService.getUserByEmailAdd(userName);

                if (StringUtils.equals(scheduleEvent.getScheduleUser().getEmailAdd(),
                        userDto.getEmailAdd())) {
                    Integer status = this.scheduleEventRepository.deleteScheduleEventByEventId(eventId, userName);
                    if (status == 1) {
                        responseWrapperDto.setStatus(RequestStatusTypes.SUCCESS.toString());
                        responseWrapperDto.setMessage("Event Deleted Successfully");
                    } else {
                        responseWrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
                        responseWrapperDto.setMessage("Failed to Delete Event");
                    }
                } else {
                    responseWrapperDto.setStatus(RequestStatusTypes.UNAUTHORIZED.toString());
                    responseWrapperDto.setMessage("Not Authorized to delete this event");
                }
            } else {
                responseWrapperDto.setStatus(RequestStatusTypes.NOT_FOUND.toString());
                responseWrapperDto.setMessage("Event not found with the provided id");
            }
            responseWrapperDto.setData(null);
            return responseWrapperDto;
        } else
            throw new ApplicationException("Event id is null or empty, please check & try again !");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public ResponseWrapperDto updateScheduleEvent(Integer eventId,
                                                  ScheduleEventDto scheduleEventDto,
                                                  Principal principal) {
        if (ObjectUtils.isNotEmpty(eventId)) {
            ResponseWrapperDto responseWrapperDto = new ResponseWrapperDto();
            Optional<ScheduleEvent> scheduleEventOptional = this.scheduleEventRepository.findById(eventId);
            if (scheduleEventOptional.isPresent()) {
                ScheduleEvent scheduleEvent = scheduleEventOptional.get();
                final String userName = principal.getName();
                YtmsUserDto userDto = this.userService.getUserByEmailAdd(userName);

                if (StringUtils.equals(scheduleEvent.getScheduleUser().getEmailAdd(),
                        userDto.getEmailAdd())) {
                    scheduleEvent.setTitle(scheduleEventDto.getTitle());
                    scheduleEvent.setStart(scheduleEventDto.getStart());
                    scheduleEvent.setEnd(scheduleEventDto.getEnd());
                    scheduleEvent.setColor(scheduleEventDto.getColor());

                    //reassigning the object with updated value
                    scheduleEvent = this.scheduleEventRepository.save(scheduleEvent);
                    scheduleEventDto = this.modelMapper.map(scheduleEvent, ScheduleEventDto.class);
                    scheduleEventDto.setScheduleUser(userDto);

                    responseWrapperDto.setStatus(RequestStatusTypes.SUCCESS.toString());
                    responseWrapperDto.setMessage("Event updated successfully");
                    responseWrapperDto.setData(scheduleEventDto);
                    return responseWrapperDto;
                } else {
                    responseWrapperDto.setStatus(RequestStatusTypes.UNAUTHORIZED.toString());
                    responseWrapperDto.setMessage("Not Authorized to update this event");
                }
            } else {
                responseWrapperDto.setStatus(RequestStatusTypes.NOT_FOUND.toString());
                responseWrapperDto.setMessage("Event not found with the provided id");
            }
            responseWrapperDto.setData(null);
            return responseWrapperDto;
        } else
            throw new ApplicationException("Event id is null or empty, please check & try again !");
    }
}
