package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.LoginHistory;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.DailyStreakDto;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.IDailyStreakService;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 13-02-2024
 */
@Service
public class IDailyStreakServiceImpl implements IDailyStreakService {
    @Autowired
    private YtmsUserRepository userRepository;
    @Override
    public DailyStreakDto getDailyStreak(String currentUserEmail) {
        Optional<YtmsUser> optionalUser = userRepository.getUserByEmail(currentUserEmail);

        if (optionalUser.isEmpty()) {
            return new DailyStreakDto().setConsequentDays(Collections.emptyList()).setStreakNumber(0);
        }

        YtmsUser currentUser = optionalUser.get();
        List<LoginHistory> loginHistoryList = currentUser.getLoginHistoryList();
        Set<LocalDate> distinctDates = loginHistoryList.stream()
                .map(LoginHistory::getLoginTime)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        List<LocalDate> consequentDays = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        int streak = 0;
        boolean yesterdayLogin = distinctDates.contains(yesterday);

        if (distinctDates.contains(today)) {
            streak = 1;
            for (int i = 0; i <= distinctDates.size(); i++) {
                if (distinctDates.contains(today.minusDays(i))) {
                    streak++;
                    consequentDays.add(today.minusDays(i));
                } else {
                    break;
                }
            }
        }

        Collections.sort(consequentDays);
        int streakNumber = yesterdayLogin ? streak : 0;

        return new DailyStreakDto().setConsequentDays(consequentDays).setStreakNumber(streakNumber);
    }
}
