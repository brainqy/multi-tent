package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */

import com.brainqy.api.domain.ScheduleEvent;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.ScheduleEventDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.ScheduleEventRepository;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IScheduleEventServiceImplTest {

    @Mock
    private ScheduleEventRepository scheduleEventRepository;

    @Mock
    private IYtmsUserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IScheduleEventServiceImpl scheduleEventService;

    private Principal principal;
    private ScheduleEventDto scheduleEventDto;
    private YtmsUserDto ytmsUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@example.com");

        scheduleEventDto = new ScheduleEventDto();
        scheduleEventDto.setEventId(1);
        scheduleEventDto.setTitle("Test Event");

        ytmsUserDto = new YtmsUserDto();
        ytmsUserDto.setEmailAdd("user@example.com");
    }

    @Test
    void createScheduleEvent_ShouldCreateEvent() {
        when(userService.getUserByEmailAdd(anyString())).thenReturn(ytmsUserDto);
        when(modelMapper.map(any(), eq(ScheduleEvent.class))).thenReturn(new ScheduleEvent());
        when(scheduleEventRepository.save(any())).thenReturn(new ScheduleEvent());
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        ScheduleEventDto result = scheduleEventService.createScheduleEvent(scheduleEventDto, principal);

        assertNotNull(result);
        assertEquals(scheduleEventDto.getEventId(), result.getEventId());
        verify(scheduleEventRepository, times(1)).save(any());
    }

    @Test
    void createScheduleEvent_ShouldThrowExceptionWhenUserNotFound() {
        when(userService.getUserByEmailAdd(anyString())).thenReturn(null);

        assertThrows(ApplicationException.class, () -> scheduleEventService.createScheduleEvent(scheduleEventDto, principal));
        verify(scheduleEventRepository, never()).save(any());
    }

    @Test
    void searchByTrainer_ShouldReturnEvents() {
        when(scheduleEventRepository.findAllEventsByTrainerEmail(anyString())).thenReturn(List.of(new ScheduleEvent()));
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        ResponseWrapperDto response = scheduleEventService.searchByTrainer("trainer@example.com");

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    void searchByTrainer_ShouldReturnNoEvents() {
        when(scheduleEventRepository.findAllEventsByTrainerEmail(anyString())).thenReturn(Collections.emptyList());

        ResponseWrapperDto response = scheduleEventService.searchByTrainer("trainer@example.com");

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertEquals("No Events Found for this trainer", response.getMessage());
    }

    @Test
    void getScheduleEventById_ShouldReturnEvent() {
        when(scheduleEventRepository.findById(anyInt())).thenReturn(Optional.of(new ScheduleEvent()));
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        ScheduleEventDto result = scheduleEventService.getScheduleEventById(1);

        assertNotNull(result);
        assertEquals(scheduleEventDto.getEventId(), result.getEventId());
    }

    @Test
    void getScheduleEventById_ShouldThrowExceptionWhenEventNotFound() {
        when(scheduleEventRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> scheduleEventService.getScheduleEventById(1));
    }

    @Test
    void getAllScheduleEvents_ShouldReturnEvents() {
        when(scheduleEventRepository.findAll()).thenReturn(List.of(new ScheduleEvent()));
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        List<ScheduleEventDto> result = scheduleEventService.getAllScheduleEvents();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllScheduleEvents_ShouldReturnEmptyList() {
        when(scheduleEventRepository.findAll()).thenReturn(Collections.emptyList());

        List<ScheduleEventDto> result = scheduleEventService.getAllScheduleEvents();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void deleteScheduleEventById_ShouldReturnNotFound() {
        when(scheduleEventRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseWrapperDto response = scheduleEventService.deleteScheduleEventById(1, principal);

        assertNotNull(response);
        assertEquals("NOT_FOUND", response.getStatus());
        assertEquals("Event not found with the provided id", response.getMessage());
    }


    @Test
    void updateScheduleEvent_ShouldReturnNotFound() {
        when(scheduleEventRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseWrapperDto response = scheduleEventService.updateScheduleEvent(1, scheduleEventDto, principal);

        assertNotNull(response);
        assertEquals("NOT_FOUND", response.getStatus());
        assertEquals("Event not found with the provided id", response.getMessage());
    }

    @Test
    void getAllScheduleEventsExceptLoggedUser_ShouldReturnEvents() {
        when(scheduleEventRepository.getAllAppointmentsExceptLoggedUser(anyString())).thenReturn(List.of(new ScheduleEvent()));
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        List<ScheduleEventDto> result = scheduleEventService.getAllScheduleEventsExceptLoggedUser(principal);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllScheduleEventsExceptLoggedUser_ShouldReturnEmptyList() {
        when(scheduleEventRepository.getAllAppointmentsExceptLoggedUser(anyString())).thenReturn(Collections.emptyList());

        List<ScheduleEventDto> result = scheduleEventService.getAllScheduleEventsExceptLoggedUser(principal);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllScheduleEventsForLoggedInUser_ShouldReturnEvents() {
        when(scheduleEventRepository.findAllEventsByTrainerEmail(anyString())).thenReturn(List.of(new ScheduleEvent()));
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        List<ScheduleEventDto> result = scheduleEventService.getAllScheduleEventsForLoggedInUser(principal);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllScheduleEventsForLoggedInUser_ShouldReturnEmptyList() {
        when(scheduleEventRepository.findAllEventsByTrainerEmail(anyString())).thenReturn(Collections.emptyList());

        List<ScheduleEventDto> result = scheduleEventService.getAllScheduleEventsForLoggedInUser(principal);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void updateScheduleEvent_ShouldUpdateEvent() {
        ScheduleEvent scheduleEvent = new ScheduleEvent();
        YtmsUser scheduleUser = new YtmsUser();
        scheduleUser.setEmailAdd("user@example.com");
        scheduleEvent.setScheduleUser(scheduleUser);

        when(scheduleEventRepository.findById(anyInt())).thenReturn(Optional.of(scheduleEvent));
        when(userService.getUserByEmailAdd(anyString())).thenReturn(ytmsUserDto);
        when(scheduleEventRepository.save(any())).thenReturn(scheduleEvent);
        when(modelMapper.map(any(), eq(ScheduleEventDto.class))).thenReturn(scheduleEventDto);

        ResponseWrapperDto response = scheduleEventService.updateScheduleEvent(1, scheduleEventDto, principal);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Event updated successfully", response.getMessage());
    }

    @Test
    void deleteScheduleEventById_ShouldDeleteEvent() {
        ScheduleEvent scheduleEvent = new ScheduleEvent();
        YtmsUser scheduleUser = new YtmsUser();
        scheduleUser.setEmailAdd("user@example.com");
        scheduleEvent.setScheduleUser(scheduleUser);

        when(scheduleEventRepository.findById(anyInt())).thenReturn(Optional.of(scheduleEvent));
        when(userService.getUserByEmailAdd(anyString())).thenReturn(ytmsUserDto);
        when(scheduleEventRepository.deleteScheduleEventByEventId(anyInt(), anyString())).thenReturn(1);

        ResponseWrapperDto response = scheduleEventService.deleteScheduleEventById(1, principal);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Event Deleted Successfully", response.getMessage());
    }
}