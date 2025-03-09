package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import com.brainqy.api.domain.*;
import com.brainqy.api.dto.*;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.*;
import com.brainqy.api.services.IServices.IYtmsUserService;
import com.brainqy.api.services.IServices.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterviewSlotServiceImplTest {

    @Mock
    private ScheduleEventRepository scheduleEventRepository;

    @Mock
    private InterviewSlotRepository interviewSlotRepository;

    @Mock
    private YtmsUserRepository userRepository;

    @Mock
    private IYtmsUserService userService;

    @Mock
    private CoinTransactionRepository transactionRepository;

    @Mock
    private JobService jobService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InterviewSlotServiceImpl interviewSlotService;

    private InterviewSlotDto interviewSlotDto;
    private InterviewSlot interviewSlot;
    private Principal principal;
    private YtmsUser ytmsUser;
    private Job job;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        interviewSlotDto = new InterviewSlotDto();
        interviewSlotDto.setId(1);
        interviewSlotDto.setDay("Monday");
        interviewSlotDto.setSlot("10:00 AM - 11:00 AM");
        interviewSlotDto.setInterviewType("Technical");
        interviewSlotDto.setHrEmail("hr@example.com");
        interviewSlotDto.setJobId(1L);

        interviewSlot = new InterviewSlot();
        interviewSlot.setId(1L);
        interviewSlot.setDay("Monday");
        interviewSlot.setSlot("10:00 AM - 11:00 AM");
        interviewSlot.setInterviewType("Technical");

        ytmsUser = new YtmsUser();
        ytmsUser.setEmailAdd("user@example.com");
        ytmsUser.setCoins(100);

        job = new Job();
        job.setId(1L);

        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@example.com");
    }

    @Test
    void saveInterviewSlot_ShouldReturnSuccess() {
        when(userService.getUserByEmailAdd("user@example.com")).thenReturn(new YtmsUserDto());
        when(modelMapper.map(any(), eq(YtmsUser.class))).thenReturn(ytmsUser);
        when(jobService.findById(1L)).thenReturn(job);
        when(modelMapper.map(any(), eq(InterviewSlot.class))).thenReturn(interviewSlot);
        when(interviewSlotRepository.save(any())).thenReturn(interviewSlot);

        ResponseWrapperDto response = interviewSlotService.saveInterviewSlot(interviewSlotDto, principal);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Interview Slot Successfully Created", response.getMessage());
        assertEquals(interviewSlotDto, response.getData());
    }

    @Test
    void getAllInterviewSlots_ShouldReturnInterviewPageDto() {
        when(userRepository.getUserByEmail("user@example.com")).thenReturn(Optional.of(ytmsUser));
        when(interviewSlotRepository.getByEmail("user@example.com")).thenReturn(Arrays.asList(interviewSlot));
        when(modelMapper.map(any(), eq(InterviewSlotDto.class))).thenReturn(interviewSlotDto);

        InterviewPageDto pageDto = interviewSlotService.getAllInterviewSlots(principal);

        assertNotNull(pageDto);
        assertEquals(100, pageDto.getCoinBalance());
        assertFalse(((List<?>) pageDto.getData()).isEmpty());
    }

    @Test
    void updateInterviewSlot_ShouldReturnUpdatedSlot() {
        when(interviewSlotRepository.findById(1L)).thenReturn(Optional.of(interviewSlot));
        when(interviewSlotRepository.save(any())).thenReturn(interviewSlot);
        when(modelMapper.map(any(), eq(InterviewSlotDto.class))).thenReturn(interviewSlotDto);

        InterviewSlotDto updatedSlot = interviewSlotService.updateInterviewSlot(1L, interviewSlotDto);

        assertNotNull(updatedSlot);
        assertEquals(interviewSlotDto.getDay(), updatedSlot.getDay());
    }

    @Test
    void updateInterviewSlot_ShouldThrowExceptionWhenSlotNotFound() {
        when(interviewSlotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> interviewSlotService.updateInterviewSlot(1L, interviewSlotDto));
    }

    @Test
    void cancelInterviewSlot_ShouldReturnUpdatedSlot() {
        when(userService.getUserByEmailAdd("user@example.com")).thenReturn(new YtmsUserDto());
        when(modelMapper.map(any(), eq(YtmsUser.class))).thenReturn(ytmsUser);
        when(interviewSlotRepository.findById(1L)).thenReturn(Optional.of(interviewSlot));
        when(interviewSlotRepository.save(any())).thenReturn(interviewSlot);
        when(modelMapper.map(any(), eq(InterviewSlotDto.class))).thenReturn(interviewSlotDto);

        InterviewSlotDto canceledSlot = interviewSlotService.cancelInterviewSlot(1L, principal);

        assertNotNull(canceledSlot);
        assertEquals("CANCELED", interviewSlot.getStatus());
    }

    @Test
    void getAllInterviewSlotsByJobId_ShouldReturnListOfSlots() {
        when(userService.getUserByEmailAdd("user@example.com")).thenReturn(new YtmsUserDto());
        when(jobService.findById(1L)).thenReturn(job);
        when(interviewSlotRepository.getAllInterviewsyJobId(any(), any())).thenReturn(Arrays.asList(interviewSlot));
        when(modelMapper.map(any(), eq(InterviewSlotDto.class))).thenReturn(interviewSlotDto);

        List<InterviewSlotDto> slots = interviewSlotService.getAllInterviewSlotsByJobId(1L, principal);

        assertNotNull(slots);
        assertFalse(slots.isEmpty());
    }
}
