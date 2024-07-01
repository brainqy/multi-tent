package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.InterviewSlot;
import com.yash.ytms.dto.InterviewPageDto;
import com.yash.ytms.dto.InterviewSlotDto;
import com.yash.ytms.dto.ResponseWrapperDto;

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
    InterviewPageDto getAllInterviewSlots();
    InterviewSlotDto updateInterviewSlot(Long id ,InterviewSlotDto interviewSlotDto);
    InterviewSlotDto cancelInterviewSlot(Long id,Principal principal);
}
