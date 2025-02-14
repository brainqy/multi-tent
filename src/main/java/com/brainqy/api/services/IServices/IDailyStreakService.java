package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.DailyStreakDto;

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
