package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.InterviewPageDto;
import com.brainqy.api.dto.InterviewSlotDto;
import com.brainqy.api.dto.ResponseWrapperDto;

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
    ResponseWrapperDto saveInterviewSlot(InterviewSlotDto interviewSlot, Principal principal);
    InterviewPageDto getAllInterviewSlots(Principal principal);
    InterviewPageDto getAllInterviewSlotsExceptLoggedInUser(Principal principal);
    InterviewSlotDto updateInterviewSlot(Long id ,InterviewSlotDto interviewSlotDto);
    InterviewSlotDto cancelInterviewSlot(Long id,Principal principal);

    List<InterviewSlotDto> getAllInterviewSlotsByJobId(long jobId, Principal principal);
}
