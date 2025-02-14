package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.CoinTransaction;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.CoinTransactionDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.repository.CoinTransactionRepository;
import com.brainqy.api.services.IServices.ICoinTransactionsService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ICoinTransactionsServiceImpl.class);

    @Autowired
    CoinTransactionRepository coinTransactionRepository;
    @Autowired
    IYtmsUserService userService;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public ResponseWrapperDto findTransactionsByUser(String email) {
        ResponseWrapperDto wrapperDto= new ResponseWrapperDto();
        List<CoinTransactionDto> coinTransactionList;
        Optional<YtmsUserDto> userDto = Optional.ofNullable(userService.getUserByEmailAdd(email));
        YtmsUser user = modelMapper.map(userDto.get(), YtmsUser.class);
        Optional<List<CoinTransaction>> coinTransactions = Optional.ofNullable(coinTransactionRepository.findByUser(user));
        if (!coinTransactions.get().isEmpty()) {
            coinTransactionList = coinTransactions.get()
                    .stream()
                    .map(se -> this
                            .modelMapper
                            .map(se, CoinTransactionDto.class))
                    .toList();
            wrapperDto.setData(coinTransactionList);
            wrapperDto.setStatus("SUCCESS");
            return  wrapperDto;
        } else
            wrapperDto.setData(null);
        wrapperDto.setMessage("No transactions Found");
        wrapperDto.setStatus("FAILED");
        return  wrapperDto;
    }

    @Override
    public Integer getUserBalance(String email) {
        YtmsUserDto userDto = this.userService.getUserByEmailAdd(email);
       return userDto.getCoins();
    }
}
