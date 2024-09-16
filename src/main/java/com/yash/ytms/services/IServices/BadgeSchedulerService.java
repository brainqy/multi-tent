package com.yash.ytms.services.IServices;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 16-09-2024
 */
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BadgeSchedulerService {

    private final BadgeService badgeService;

    public BadgeSchedulerService(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    // This method runs every 12 hours
    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)  // 12 hours in milliseconds
    public void checkAndAssignBadges() {
        // Assuming badgeService has logic to find all users and check eligibility
        badgeService.assignEligibleBadgesToAllUsers();
    }
}
