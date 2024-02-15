package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.CoinTransaction;
import com.yash.ytms.domain.Referral;
import com.yash.ytms.domain.TransactionType;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.repository.CoinTransactionRepository;
import com.yash.ytms.repository.ReferralRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IReferralService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

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
    private ModelMapper modelMapper;
    @Autowired
    CoinTransactionRepository coinTransactionRepository;

    @Override
    public void setReferralEntity(YtmsUserDto userDto) {
        YtmsUser referrer = userRepository.getUserByEmail(userDto.getRef());
        if (referrer != null) {
            Referral referral = new Referral();
            referral .setReferralCode(userDto.getRef().toLowerCase());
            referral.setEmail(userDto.getEmailAdd());
            referral.setStatus("pending");
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
                    .setTransactionType(TransactionType.REFERRAL_BONUS)
                    .setCreatedDate(Date.from(Instant.now()));
            coinTransactionRepository.save(coinTransaction);

            referrer.setCoins(referrer.getCoins() + COINS_PER_REFERRAL);
            userRepository.save(referrer);
        }

    }
}
