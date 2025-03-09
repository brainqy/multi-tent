package com.brainqy.api.util;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-03-2025
 */
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.YtmsUserDto;
import org.springframework.stereotype.Component;

@Component
public class YtmsUserConverter {

    public static YtmsUserDto convertToDto(YtmsUser entity) {
        if (entity == null) {
            return null;
        }

        YtmsUserDto dto = new YtmsUserDto();
        dto.setFullName(entity.getFullName());
        dto.setEmailAdd(entity.getEmailAdd());
        dto.setPassword(entity.getPassword());
        dto.setConfirmPassword(entity.getConfirmPassword());
        dto.setAccountStatus(entity.getAccountStatus());
        dto.setCoins(entity.getCoins());
        dto.setOrganizationId(entity.getOrganization() != null ? entity.getOrganization().getId() : null);
        dto.setXpPoints(entity.getXpPoints());
        dto.setTasksCompleted(entity.getTasksCompleted());
        dto.setCoursesCompleted(entity.getCoursesCompleted());
        return dto;
    }
}