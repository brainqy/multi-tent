package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.CoinTransaction;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.CoinTransactionDto;
import com.yash.ytms.dto.ReferralDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.exception.ApplicationException;
import com.yash.ytms.repository.CoinTransactionRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.ICoinTransactionsService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 22-02-2024
 */
@Service
public class ICoinTransactionsServiceImpl implements ICoinTransactionsService {
    @Autowired
    CoinTransactionRepository coinTransactionRepository;
    @Autowired
    IYtmsUserService userService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<CoinTransactionDto> findTransactionsByUser(String email) {
        YtmsUserDto userDto = userService.getUserByEmailAdd(email);
        YtmsUser user = modelMapper.map(userDto, YtmsUser.class);
        List<CoinTransaction> coinTransactions = coinTransactionRepository.findByUser(user);
        if (!coinTransactions.isEmpty()) {
            return coinTransactions
                    .stream()
                    .map(se -> this
                            .modelMapper
                            .map(se, CoinTransactionDto.class))
                    .toList();
        } else
            throw new ApplicationException("No CoinTransactions  found !");
    }

    @Override
    public Integer getUserBalance(String email) {
        YtmsUserDto userDto = this.userService.getUserByEmailAdd(email);
       return userDto.getCoins();
    }
}
