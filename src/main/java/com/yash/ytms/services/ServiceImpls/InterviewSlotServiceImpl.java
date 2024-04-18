package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.InterviewSlot;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.InterviewSlotDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.InterviewSlotRepository;
import com.yash.ytms.services.IServices.IYtmsUserService;
import com.yash.ytms.services.IServices.InterviewSlotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-04-2024
 */
@Service
public class InterviewSlotServiceImpl implements InterviewSlotService {
  @Autowired
    private InterviewSlotRepository interviewSlotRepository;
  @Autowired
  private ModelMapper modelMapper;
    @Autowired
    private IYtmsUserService userService;


    @Override
    public InterviewSlotDto saveInterviewSlot(InterviewSlotDto interviewSlotDto, Principal principal) {
        final String userName = principal.getName();
        YtmsUserDto userDto = this.userService.getUserByEmailAdd(userName);
        YtmsUser ytmsUser = modelMapper.map(userDto, YtmsUser.class);
        InterviewSlot interviewSlot =modelMapper.map(interviewSlotDto,InterviewSlot.class);
        interviewSlot.setScheduleUser(ytmsUser);
         interviewSlotRepository.save(interviewSlot);
         return  interviewSlotDto;
    }


    @Override
    public List<InterviewSlotDto> getAllInterviewSlots() {
        List<InterviewSlot> slots = interviewSlotRepository.findAll();
        // Map each InterviewSlot entity to InterviewSlotDto
        List<InterviewSlotDto> slotDtos = slots.stream()
                .map(slot -> modelMapper.map(slot, InterviewSlotDto.class))
                .collect(Collectors.toList());
        return slotDtos;
    }

    @Override
    public InterviewSlotDto updateInterviewSlot(Long id, InterviewSlotDto updatedSlotDto) {
        Optional<InterviewSlot> optionalSlot = interviewSlotRepository.findById(id);
        if (optionalSlot.isPresent()) {
            InterviewSlot existingSlot = optionalSlot.get();

            // Update the fields with new values
            existingSlot.setDay(updatedSlotDto.getDay());
            existingSlot.setSlot(updatedSlotDto.getSlot());
            existingSlot.setInterviewType(updatedSlotDto.getInterviewType());
            existingSlot.setKindOfInterviewType(updatedSlotDto.getKindOfInterviewType());

            // Save the updated slot
            InterviewSlot updatedSlot = interviewSlotRepository.save(existingSlot);

            // Convert the updated entity to DTO and return
            return modelMapper.map(updatedSlot, InterviewSlotDto.class);
        } else {
            throw new ApplicationException("Interview slot not found with id: " + id);
        }
    }

    @Override
    public InterviewSlotDto cancelInterviewSlot(Long id) {
        Optional<InterviewSlot> interviewSlot = this.interviewSlotRepository.findById(id);
        InterviewSlot updatedSlot = null;
        if (interviewSlot.isPresent()) {
            InterviewSlot existingSlot = interviewSlot.get();
            existingSlot.setStatus("CANCELED");
            updatedSlot = interviewSlotRepository.save(existingSlot);
        }
        return modelMapper.map(updatedSlot, InterviewSlotDto.class);
    }
}
