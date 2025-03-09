package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import com.brainqy.api.domain.Referral;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.ReferralDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.repository.CoinTransactionRepository;
import com.brainqy.api.repository.ReferralRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.ServiceImpls.IReferralServiceImpl;
import com.brainqy.api.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IReferralServiceImplTest {

    @Mock
    private YtmsUserRepository userRepository;

    @Mock
    private ReferralRepository referralRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CoinTransactionRepository coinTransactionRepository;

    @InjectMocks
    private IReferralServiceImpl referralService;

    private YtmsUserDto userDto;
    private YtmsUser user;
    private Referral referral;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new YtmsUserDto();
        userDto.setEmailAdd("user@example.com");
        userDto.setRef("referrer@example.com");

        user = new YtmsUser();
        user.setEmailAdd("referrer@example.com");
        user.setCoins(0);

        referral = new Referral();
        referral.setReferralCode("referrer@example.com");
        referral.setEmail("user@example.com");
        referral.setStatus("approved");
        referral.setReferrer(user);
        referral.setReferredAt(LocalDateTime.now());
    }

    @Test
    void setReferralEntity_ShouldSetReferral() {
        when(userRepository.getUserByEmail("referrer@example.com")).thenReturn(Optional.of(user));
        when(referralRepository.save(any())).thenReturn(referral);
        when(userRepository.save(any())).thenReturn(user);

        referralService.setReferralEntity(userDto);

        verify(referralRepository, times(1)).save(any());
        verify(userRepository, times(2)).save(any());
        verify(coinTransactionRepository, times(1)).save(any());
    }

    @Test
    void setReferralEntity_ShouldDoNothingWhenReferrerNotFound() {
        when(userRepository.getUserByEmail("referrer@example.com")).thenReturn(Optional.empty());

        referralService.setReferralEntity(userDto);

        verify(referralRepository, never()).save(any());
        verify(userRepository, never()).save(any());
        verify(coinTransactionRepository, never()).save(any());
    }

    @Test
    void findByReferrerId_ShouldReturnReferrals() {
        when(userRepository.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(referralRepository.findByReferrerId(user)).thenReturn(List.of(referral));
        when(modelMapper.map(referral, ReferralDto.class)).thenReturn(new ReferralDto());

        List<ReferralDto> referrals = referralService.findByReferrerId(userDto);

        assertNotNull(referrals);
        assertFalse(referrals.isEmpty());
        verify(referralRepository, times(1)).findByReferrerId(user);
    }

    @Test
    void findByReferrerId_ShouldReturnEmptyListWhenNoReferrals() {
        when(userRepository.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(referralRepository.findByReferrerId(user)).thenReturn(Collections.emptyList());

        List<ReferralDto> referrals = referralService.findByReferrerId(userDto);

        assertNotNull(referrals);
        assertTrue(referrals.isEmpty());
        verify(referralRepository, times(1)).findByReferrerId(user);
    }

    @Test
    void findByReferrerId_ShouldReturnEmptyListWhenUserNotFound() {
        when(userRepository.getUserByEmail("user@example.com")).thenReturn(Optional.empty());

        List<ReferralDto> referrals = referralService.findByReferrerId(userDto);

        assertNotNull(referrals);
        assertTrue(referrals.isEmpty());
        verify(referralRepository, never()).findByReferrerId(any());
    }

    @Test
    void sendEmailToReferral_ShouldSendEmailWhenUserNotFound() throws MessagingException {
        when(userRepository.getUserByEmail("user@example.com")).thenReturn(Optional.empty());

        ResponseEntity<ResponseWrapperDto> response = referralService.sendEmailToReferral("user@example.com");

        assertNotNull(response);
        assertEquals("SUCCESS", response.getBody().getStatus());
        verify(emailUtil, times(1)).pendingReferralEmail("user@example.com");
    }

    @Test
    void sendEmailToReferral_ShouldNotSendEmailWhenUserFound() throws MessagingException {
        when(userRepository.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<ResponseWrapperDto> response = referralService.sendEmailToReferral("user@example.com");

        assertNotNull(response);
        assertEquals("FAILED", response.getBody().getStatus());
        verify(emailUtil, never()).pendingReferralEmail(any());
    }

    @Test
    void getMyReferralLink_ShouldReturnEncodedEmail() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getName()).thenReturn("user@example.com");

        String referralLink = referralService.getMyReferralLink();

        assertNotNull(referralLink);
        assertEquals("dXNlckBleGFtcGxlLmNvbQ==", referralLink);
    }
}
