package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.*;
import com.yash.ytms.dto.*;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.CoinTransactionRepository;
import com.yash.ytms.repository.InterviewSlotRepository;
import com.yash.ytms.repository.ScheduleEventRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.security.jwt.JwtAuthenticationFilter;
import com.yash.ytms.services.IServices.IYtmsUserService;
import com.yash.ytms.services.IServices.InterviewSlotService;
import com.yash.ytms.services.IServices.JobService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewSlotServiceImpl.class);
    @Autowired
    private ScheduleEventRepository scheduleEventRepository;

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
    @Autowired
    private JobService jobService;


    @Override
    public ResponseWrapperDto saveInterviewSlot(InterviewSlotDto interviewSlotDto, Principal principal) {
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        final String userName = principal.getName();
        Optional<YtmsUserDto> userDto = Optional.ofNullable(this.userService.getUserByEmailAdd(userName));
             YtmsUser ytmsUser = modelMapper.map(userDto, YtmsUser.class);
        if (interviewSlotDto.getHrEmail() != null &&interviewSlotDto.getJobId()!=0) {
            Optional<Job> jobOptional = Optional.ofNullable(this.jobService.findById(interviewSlotDto.getJobId()));
            InterviewSlot interviewSlot =modelMapper.map(interviewSlotDto,InterviewSlot.class);
            interviewSlot.setJob(jobOptional.get());
            interviewSlot.setScheduleUser(ytmsUser);
            interviewSlotRepository.save(interviewSlot);
            wrapperDto.setMessage("Interview Slot Successfully Created");
            wrapperDto.setStatus("SUCCESS");
            wrapperDto.setData(interviewSlotDto);
        }
        if(interviewSlotDto.getInterviewType()!=null){
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
                wrapperDto.setStatus("Coin Balance is not enough for Interview slot creation");
                wrapperDto.setStatus("FAILED");
            }
        }


        return  wrapperDto;
    }


    @Override
    public InterviewPageDto getAllInterviewSlots(Principal principal) {
        String userEmail= principal.getName();
        InterviewPageDto pageDto= new InterviewPageDto();
        Optional<YtmsUser> user = userRepository.getUserByEmail(userEmail);
        Integer userCoinBalance = user.get().getCoins();
        List<InterviewSlot> slots = interviewSlotRepository.getByEmail(userEmail);
        // Map each InterviewSlot entity to InterviewSlotDto
        List<InterviewSlotDto> slotDtos = slots.stream()
                .map(slot -> modelMapper.map(slot, InterviewSlotDto.class))
                .collect(Collectors.toList());
        pageDto.setCoinBalance(userCoinBalance);
        pageDto.setData(slotDtos);
        return pageDto;
    }

    @Override
    public InterviewPageDto getAllInterviewSlotsExceptLoggedInUser(Principal principal) {
        String userEmail= principal.getName();
        InterviewPageDto pageDto= new InterviewPageDto();
        Optional<YtmsUser> user = userRepository.getUserByEmail(userEmail);
        Integer userCoinBalance = user.get().getCoins();
        List<InterviewSlot> slots = interviewSlotRepository.getAllInterviewSlotsExceptLoggedUser(userEmail);
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
        if(interviewSlot.get().getInterviewType().startsWith("real")){
            System.out.println("Interview is real");
            return modelMapper.map(updatedSlot, InterviewSlotDto.class);
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

    @Override
    public List<InterviewSlotDto> getAllInterviewSlotsByJobId(long jobId, Principal principal) {
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        final String userName = principal.getName();
        Optional<YtmsUserDto> userDto = Optional.ofNullable(this.userService.getUserByEmailAdd(userName));
        Optional<Job> jobOptional = Optional.ofNullable(this.jobService.findById(jobId));
        List<InterviewSlot> interviewSlots= interviewSlotRepository.getAllInterviewsyJobId(jobOptional.get(),userDto.get().getEmailAdd());
        List<InterviewSlotDto> slotDtos = interviewSlots.stream()
                .map(slot -> modelMapper.map(slot, InterviewSlotDto.class))
                .collect(Collectors.toList());
        return  slotDtos;

    }
    void getAllAvailableInterviewSlots(){
        List<ScheduleEvent> allevents = scheduleEventRepository.findAll();
    }


}
