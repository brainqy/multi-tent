package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.BadgeDto;
import com.yash.ytms.dto.YtmsUserDto;

import java.security.Principal;
import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 14-09-2024
 */
public interface BadgeService {
    void createBadge(BadgeDto badgeDto);
    List<BadgeDto> getAllBadges();
    BadgeDto findBadgeById(Long id);
    BadgeDto findBadgeByName(String name);
    boolean checkBadgeEligibility(YtmsUserDto user, BadgeDto badge);  // Check if the user can earn a badge

    List<BadgeDto> assignBadgesToUser(YtmsUserDto user);  // Assign eligible badges to the user

    void assignEligibleBadgesToAllUsers();

    List<BadgeDto> getAssignedBadgesByUserName(Principal principal);
}
