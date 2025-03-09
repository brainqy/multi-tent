package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 06-03-2025
 */
import com.brainqy.api.domain.CoinTransaction;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.CoinTransactionDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.repository.CoinTransactionRepository;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ICoinTransactionsServiceImplTest {

    @Mock
    private CoinTransactionRepository coinTransactionRepository;

    @Mock
    private IYtmsUserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ICoinTransactionsServiceImpl coinTransactionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findTransactionsByUser_shouldReturnTransactions_whenTransactionsExist() {
        String email = "dvsomwanshi@gmail.com";
        YtmsUserDto userDto = new YtmsUserDto();
        userDto.setEmailAdd(email);
        YtmsUser user = new YtmsUser();
        user.setEmailAdd(email);

        CoinTransaction transaction = new CoinTransaction();
        transaction.setId(1L);
        transaction.setUser(user);

        when(userService.getUserByEmailAdd(anyString())).thenReturn(userDto);
        when(modelMapper.map(userDto, YtmsUser.class)).thenReturn(user);
        when(coinTransactionRepository.findByUser(user)).thenReturn(List.of(transaction));
        when(modelMapper.map(transaction, CoinTransactionDto.class)).thenReturn(new CoinTransactionDto());

        ResponseWrapperDto response = coinTransactionsService.findTransactionsByUser(email);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(1, ((List<?>) response.getData()).size());
    }

    @Test
    void findTransactionsByUser_shouldReturnEmpty_whenNoTransactionsExist() {
        String email = "dvsomwanshi@gmail.com";
        YtmsUserDto userDto = new YtmsUserDto();
        userDto.setEmailAdd(email);
        YtmsUser user = new YtmsUser();
        user.setEmailAdd(email);

        when(userService.getUserByEmailAdd(anyString())).thenReturn(userDto);
        when(modelMapper.map(userDto, YtmsUser.class)).thenReturn(user);
        when(coinTransactionRepository.findByUser(user)).thenReturn(Collections.emptyList());

        ResponseWrapperDto response = coinTransactionsService.findTransactionsByUser(email);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertNull(response.getData());
        assertEquals("No transactions Found", response.getMessage());
    }

    @Test
    void findTransactionsByUser_shouldHandleNullUser() {
       String email = "dvsomwanshi@gmail.com";
       YtmsUserDto userDto = new YtmsUserDto();
       userDto.setEmailAdd(email);
       userDto.setCoins(0); // or any default value

        when(userService.getUserByEmailAdd(anyString())).thenReturn(userDto);
        ResponseWrapperDto response = coinTransactionsService.findTransactionsByUser(email);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertNull(response.getData());
        assertEquals("No transactions Found", response.getMessage());
    }

    @Test
    void getUserBalance_shouldReturnUserBalance_whenUserExists() {
        String email = "dvsomwanshi@gmail.com";
        YtmsUserDto userDto = new YtmsUserDto();
        userDto.setEmailAdd(email);
        userDto.setCoins(100);

        when(userService.getUserByEmailAdd(anyString())).thenReturn(userDto);

        Integer balance = coinTransactionsService.getUserBalance(email);

        assertNotNull(balance);
        assertEquals(100, balance);
    }

    @Test
    void getUserBalance_shouldHandleNullUser() {
        String email = "dvsomwanshi@gmail.com";
        YtmsUserDto userDto = new YtmsUserDto();
        userDto.setEmailAdd(email);
        userDto.setCoins(0); // or any default value

        when(userService.getUserByEmailAdd(anyString())).thenReturn(userDto);

        Integer balance = coinTransactionsService.getUserBalance(email);

        assertNotNull(balance);
        assertEquals(0, balance); // or the default value set above
    }
}
