package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.InterviewSlot;
import com.yash.ytms.dto.InterviewSlotDto;

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
public interface InterviewSlotService {
    InterviewSlotDto saveInterviewSlot(InterviewSlotDto interviewSlot, Principal principal);
    List<InterviewSlotDto> getAllInterviewSlots();
    InterviewSlotDto updateInterviewSlot(Long id ,InterviewSlotDto interviewSlotDto);
    InterviewSlotDto cancelInterviewSlot(Long id);
}
