package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.*;
import com.yash.ytms.dto.*;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.CoinTransactionRepository;
import com.yash.ytms.repository.InterviewSlotRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IYtmsUserService;
import com.yash.ytms.services.IServices.InterviewSlotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yash.ytms.constants.AppConstants.INTERVIEW_CHARGE;

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
  private YtmsUserRepository userRepository;
  @Autowired
  private ModelMapper modelMapper;
    @Autowired
    private IYtmsUserService userService;
    @Autowired
    private CoinTransactionRepository transactionRepository;


    @Override
    public ResponseWrapperDto saveInterviewSlot(InterviewSlotDto interviewSlotDto, Principal principal) {
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        final String userName = principal.getName();
        Optional<YtmsUserDto> userDto = Optional.ofNullable(this.userService.getUserByEmailAdd(userName));
        YtmsUser ytmsUser = modelMapper.map(userDto, YtmsUser.class);
        int remainingCoinBalance = ytmsUser.getCoins() - INTERVIEW_CHARGE;
        if (remainingCoinBalance>0){
            InterviewSlot interviewSlot =modelMapper.map(interviewSlotDto,InterviewSlot.class);
            interviewSlot.setScheduleUser(ytmsUser);
            interviewSlotRepository.save(interviewSlot);
            //coin transaction
            ytmsUser.setCoins(remainingCoinBalance);
            CoinTransactionDto transactionDto= new CoinTransactionDto();
            transactionDto.setSourceType(SourceType.INTERVIEW);
            transactionDto.setTransactionType(TransactionType.DEBIT);
            transactionDto.setAmount(25);
            transactionDto.setUser(userDto.get());
            transactionDto.setCreatedDate(Date.from(Instant.now()));
            userRepository.save(ytmsUser);
            CoinTransaction transactionData=modelMapper.map(transactionDto, CoinTransaction.class);
            transactionRepository.save(transactionData);
            wrapperDto.setMessage("Interview Slot Successfully Created");
            wrapperDto.setStatus("SUCCESS");
            wrapperDto.setData(interviewSlotDto);
        }else {
            wrapperDto.setStatus("COin Balance is not enough for Interview slot creation");
            wrapperDto.setStatus("FAILED");
        }

        return  wrapperDto;
    }


    @Override
    public InterviewPageDto getAllInterviewSlots() {
        InterviewPageDto pageDto= new InterviewPageDto();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        Optional<YtmsUser> user = userRepository.getUserByEmail(userEmail);
        Integer userCoinBalance = user.get().getCoins();
        List<InterviewSlot> slots = interviewSlotRepository.findAll();
        // Map each InterviewSlot entity to InterviewSlotDto
        List<InterviewSlotDto> slotDtos = slots.stream()
                .map(slot -> modelMapper.map(slot, InterviewSlotDto.class))
                .collect(Collectors.toList());
        pageDto.setCoinBalance(userCoinBalance);
        pageDto.setData(slotDtos);
        return pageDto;
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
    public InterviewSlotDto cancelInterviewSlot(Long id,Principal principal) {
        final String userName = principal.getName();
        Optional<YtmsUserDto> userDto = Optional.ofNullable(this.userService.getUserByEmailAdd(userName));
        YtmsUser ytmsUser = modelMapper.map(userDto, YtmsUser.class);
        Optional<InterviewSlot> interviewSlot = this.interviewSlotRepository.findById(id);
        InterviewSlot updatedSlot = null;
        if (interviewSlot.isPresent()) {
            InterviewSlot existingSlot = interviewSlot.get();
            existingSlot.setStatus("CANCELED");
            updatedSlot = interviewSlotRepository.save(existingSlot);
        }
        int remainingBalance=ytmsUser.getCoins()+INTERVIEW_CHARGE;
        ytmsUser.setCoins(remainingBalance);
        userRepository.save(ytmsUser);
        CoinTransactionDto transactionDto= new CoinTransactionDto();
        transactionDto.setSourceType(SourceType.INTERVIEW);
        transactionDto.setTransactionType(TransactionType.CREDIT);
        transactionDto.setAmount(25);
        transactionDto.setUser(userDto.get());
        transactionDto.setCreatedDate(Date.from(Instant.now()));
        transactionRepository.save(modelMapper.map(transactionDto, CoinTransaction.class));
        return modelMapper.map(updatedSlot, InterviewSlotDto.class);
    }
}
