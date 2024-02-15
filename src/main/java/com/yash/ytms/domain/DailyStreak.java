package com.yash.ytms.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 12-02-2024
 */
public class DailyStreak {

    private LocalDate lastDate;
    private int streakCount;

    public DailyStreak(LocalDate lastDate, int streakCount) {
        this.lastDate = lastDate;
        this.streakCount = streakCount;
    }

    public void updateStreak() {
        LocalDate today = LocalDate.now();
        if (ChronoUnit.DAYS.between(lastDate, today) == 1) {
            streakCount++;
        } else if (ChronoUnit.DAYS.between(lastDate, today) > 1) {
            streakCount = 1;
        }
        lastDate = today;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public int getStreakCount() {
        return streakCount;
    }
}