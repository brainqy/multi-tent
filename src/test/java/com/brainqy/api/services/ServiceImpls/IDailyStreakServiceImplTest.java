package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.brainqy.api.domain.LoginHistory;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.DailyStreakDto;
import com.brainqy.api.repository.YtmsUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class IDailyStreakServiceImplTest {

    @InjectMocks
    private IDailyStreakServiceImpl dailyStreakService;

    @Mock
    private YtmsUserRepository userRepository;

    private YtmsUser user;
    private List<LoginHistory> loginHistoryList;

    @BeforeEach
    void setUp() {
        user = new YtmsUser();
        user.setEmailAdd("dvsomwanshi@gmail.com");
        user.setCoins(100);
        loginHistoryList = new ArrayList<>();
        user.setLoginHistoryList(loginHistoryList);
    }

    @Test
    void shouldReturnZeroStreak_WhenUserNotFound() {
        // Arrange
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act
        DailyStreakDto result = dailyStreakService.getDailyStreak("test@example.com");

        // Assert
        assertEquals(0, result.getStreakNumber());
        assertTrue(result.getConsequentDays().isEmpty());
    }

    @Test
    void shouldReturnZeroStreak_WhenUserHasNoLoginHistory() {
        // Arrange
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        DailyStreakDto result = dailyStreakService.getDailyStreak("test@example.com");

        // Assert
        assertEquals(1, result.getStreakNumber()); // If no streak history, the minimum is 1
        assertTrue(result.getConsequentDays().isEmpty());
    }

    @Test
    void shouldCalculateCorrectStreak_WhenUserHasConsecutiveLogins() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);

        loginHistoryList.add(createLoginHistory(today));
        loginHistoryList.add(createLoginHistory(yesterday));
        loginHistoryList.add(createLoginHistory(twoDaysAgo));
        user.setLoginHistoryList(loginHistoryList);

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        DailyStreakDto result = dailyStreakService.getDailyStreak("test@example.com");

        // Assert
        assertEquals(3, result.getStreakNumber());
        assertEquals(3, result.getConsequentDays().size());
        assertTrue(result.getConsequentDays().contains(today));
        assertTrue(result.getConsequentDays().contains(yesterday));
        assertTrue(result.getConsequentDays().contains(twoDaysAgo));
    }

    @Test
    void shouldResetStreak_WhenUserMissesADay() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate twoDaysAgo = today.minusDays(2); // Missed yesterday

        loginHistoryList.add(createLoginHistory(today));
        loginHistoryList.add(createLoginHistory(twoDaysAgo));
        user.setLoginHistoryList(loginHistoryList);

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        DailyStreakDto result = dailyStreakService.getDailyStreak("test@example.com");

        // Assert
        assertEquals(1, result.getStreakNumber()); // Streak resets because yesterday was missed
        assertEquals(1, result.getConsequentDays().size());
        assertTrue(result.getConsequentDays().contains(today));
    }

    @Test
    void shouldReturnCorrectBalance_WhenFetchingStreak() {
        // Arrange
        LocalDate today = LocalDate.now();
        loginHistoryList.add(createLoginHistory(today));
        user.setLoginHistoryList(loginHistoryList);
        user.setCoins(200);

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        DailyStreakDto result = dailyStreakService.getDailyStreak("test@example.com");

        // Assert
        assertEquals(200, result.getUserBalance());
    }

    private LoginHistory createLoginHistory(LocalDate date) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setLoginTime(date.atStartOfDay());
        return loginHistory;
    }
}
