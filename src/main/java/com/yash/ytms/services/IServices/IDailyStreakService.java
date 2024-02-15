package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.DailyStreakDto;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 13-02-2024
 */
public interface IDailyStreakService {
    DailyStreakDto getDailyStreak(String email) ;
}
