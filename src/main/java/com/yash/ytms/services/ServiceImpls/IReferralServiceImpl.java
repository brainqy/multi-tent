package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.constants.RequestStatusTypes;
import com.yash.ytms.domain.*;
import com.yash.ytms.dto.ReferralDto;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.repository.CoinTransactionRepository;
import com.yash.ytms.repository.ReferralRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IReferralService;
import com.yash.ytms.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 14-02-2024
 */
@Service
public class IReferralServiceImpl implements IReferralService {
    private static final Integer COINS_PER_REFERRAL = 10;
    @Autowired
    private YtmsUserRepository userRepository;
    @Autowired
    private ReferralRepository referralRepository;
    @Autowired
    private EmailUtil emailUtil;



    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    CoinTransactionRepository coinTransactionRepository;

    @Override
    public void setReferralEntity(YtmsUserDto userDto) {
        Optional<YtmsUser> optionalReferrer = userRepository.getUserByEmail(userDto.getRef());

        optionalReferrer.ifPresent(referrer -> {
            Referral referral = new Referral();
            referral.setReferralCode(userDto.getRef().toLowerCase());
            referral.setEmail(userDto.getEmailAdd());
            referral.setStatus(referral.getReferralCode() != null ? "approved" : "pending");
            referral.setReferrer(referrer);
            referral.setReferredAt(LocalDateTime.now());

            referralRepository.save(referral);

            referrer.getReferrals().add(referral);
            referrer.setCoins(referrer.getCoins() + COINS_PER_REFERRAL);
            userRepository.save(referrer);

            userDto.setCoins(COINS_PER_REFERRAL);

            CoinTransaction coinTransaction = new CoinTransaction()
                    .setUser(referrer)
                    .setAmount(COINS_PER_REFERRAL)
                    .setSourceType(SourceType.REFERRAL_BONUS)
                    .setTransactionType(TransactionType.CREDIT)
                    .setCreatedDate(Date.from(Instant.now()));

            coinTransactionRepository.save(coinTransaction);

            referrer.setCoins(referrer.getCoins() + COINS_PER_REFERRAL);
            userRepository.save(referrer);
        });
    }

    @Override
    public List<ReferralDto> findByReferrerId(YtmsUserDto userDto) {
        Optional<YtmsUser> optionalUser = userRepository.getUserByEmail(userDto.getEmailAdd());

        if (optionalUser.isEmpty()) {
            System.out.println("User not found");
            return List.of();
        }

        YtmsUser user = optionalUser.get();
        List<Referral> referrals = referralRepository.findByReferrerId(user);

        if (referrals.isEmpty()) {
            System.out.println("No referrals found");
            return List.of();
        }

        return referrals.stream()
                .map(referral -> modelMapper.map(referral, ReferralDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ResponseWrapperDto> sendEmailToReferral(String referralEmail) throws MessagingException {
        ResponseWrapperDto wrapperDto = new ResponseWrapperDto();
        Optional<YtmsUser> userExists = userRepository.getUserByEmail(referralEmail);

        if (userExists.isEmpty()) {
            emailUtil.pendingReferralEmail(referralEmail);
            wrapperDto.setStatus(RequestStatusTypes.SUCCESS.toString());
            wrapperDto.setMessage("Email is sent to referral successfully");
        } else {
            wrapperDto.setStatus(RequestStatusTypes.FAILED.toString());
            wrapperDto.setMessage("User already registered");
        }

        return new ResponseEntity<>(wrapperDto, HttpStatus.OK);
    }

    @Override
    public String getMyReferralLink() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser= auth.getName();
       String encodedEmail=  Base64.getEncoder().encodeToString(currentUser.getBytes());

       return encodedEmail;
    }
}
