package com.brainqy.api.services.ServiceImpls;

import com.brainqy.api.domain.Badge;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.BadgeDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.repository.BadgeRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.IServices.BadgeService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 14-09-2024
 */
@Service
public class IBadgeServiceImpl implements BadgeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IBadgeServiceImpl.class);

    @Autowired
    private BadgeRepository badgeRepository;
    @Autowired
    private YtmsUserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private IYtmsUserService userService;

@Override
public void createBadge(BadgeDto badgeDto) {
    if (badgeDto.getName() == null || badgeDto.getName().isEmpty()) {
        throw new IllegalArgumentException("Badge name cannot be empty");
    }
    if (badgeDto.getDescription() == null) {
        throw new IllegalArgumentException("Badge description cannot be null");
    }
    if (badgeDto.getThreshold() < 0) {
        throw new IllegalArgumentException("Badge threshold must be non-negative");
    }

    Badge badge = new Badge();
    badge.setName(badgeDto.getName());
    badge.setDescription(badgeDto.getDescription());
    badge.setIcon(badgeDto.getIcon());
    badge.setBackgroundColor(badgeDto.getBackgroundColor());
    badge.setRule(badgeDto.getRule());
    badge.setThreshold(badgeDto.getThreshold());
    badgeRepository.save(badge);
}

    @Override
    public List<BadgeDto> getAllBadges() {
        return badgeRepository.findAll().stream()
                .map(badge -> mapper.map(badge, BadgeDto.class))  // Use the mapper to convert Badge to BadgeDto
                .collect(Collectors.toList());
    }


    @Override
    public BadgeDto findBadgeById(Long id) {
        return badgeRepository.findById(id)
                .map(badge -> mapper.map(badge, BadgeDto.class))  // Use the mapper to convert Badge to BadgeDto
                .orElse(null);
    }


    @Override
    public BadgeDto findBadgeByName(String name) {
        Badge badge = badgeRepository.findByNameIgnoreCase(name);
        return badge != null ? mapper.map(badge, BadgeDto.class) : null;  // Use the mapper to convert Badge to BadgeDto
    }


    @Override
    public boolean checkBadgeEligibility(YtmsUserDto user, BadgeDto badge) {
        switch (badge.getRule()) {
            case XP_BASED:
                return user.getXpPoints() >= badge.getThreshold();
            case TASK_COMPLETION:
                return user.getTasksCompleted() >= badge.getThreshold();
            case COURSE_COMPLETION:
                return user.getCoursesCompleted() >= badge.getThreshold();
            case TIME_BASED:
                // Custom logic for time-based badges (e.g., account age)
                return checkUserMembershipDuration(user);
            default:
                return false;
        }
    }

    private boolean checkUserMembershipDuration(YtmsUserDto user) {
        return  true;
    }

    @Override
    public List<BadgeDto> assignBadgesToUser(YtmsUserDto userDto) {
        List<BadgeDto> eligibleBadges = new ArrayList<>();
        List<Badge> allBadges = badgeRepository.findAll();

        // Convert YtmsUserDto to YtmsUser entity before making changes
        YtmsUser user = mapper.map(userDto, YtmsUser.class);

        for (Badge badge : allBadges) {
            // Convert Badge to BadgeDto
            BadgeDto badgeDto = mapper.map(badge, BadgeDto.class);

            // Check eligibility using the badgeDto
            if (checkBadgeEligibility(userDto, badgeDto)) {
                eligibleBadges.add(badgeDto); // Add BadgeDto to the result list
                user.getEarnedBadges().add(badge); // Add the Badge entity to the user's earned badges
            }
        }

        // Save the updated user entity back to the database
        userRepository.save(user);

        return eligibleBadges; // Return the list of eligible BadgeDtos
    }

    @Override
    public void assignEligibleBadgesToAllUsers() {
        List<YtmsUserDto> users = userService.getAllUsers();  // Fetch all users

        for (YtmsUserDto user : users) {
            // Logic to check and assign badges for each user
            List<BadgeDto> eligibleBadges = checkUserForEligibleBadges(user);
            if (!eligibleBadges.isEmpty()) {
                assignBadgesToUser(user);
            }
        }
    }

    @Override
    @Transactional
    public List<BadgeDto> getAssignedBadgesByUserName(Principal principal) {
        String userName=principal.getName();
        List<Badge> badges = badgeRepository.getAssignedBadgesByUserName(userName);
        return badges.stream().map(badge->mapper.map(badge,BadgeDto.class)).collect(Collectors.toList());

    }

    public List<BadgeDto> checkUserForEligibleBadges(YtmsUserDto user) {
        List<BadgeDto> eligibleBadges = new ArrayList<>();
        List<Badge> allBadges = badgeRepository.findAll();  // Fetch all available badges

        for (Badge badge : allBadges) {
            BadgeDto badgeDto = mapper.map(badge,BadgeDto.class);  // Convert entity to DTO for easier comparison
            if (isUserEligibleForBadge(user, badgeDto)) {
                eligibleBadges.add(badgeDto);  // Add to eligible list if user meets the criteria
            }
        }

        return eligibleBadges;
    }
    private boolean isUserEligibleForBadge(YtmsUserDto user, BadgeDto badge) {
        switch (badge.getRule()) {
            case XP_BASED:
                return user.getXpPoints() >= badge.getThreshold();
            case TASK_COMPLETION:
                return user.getTasksCompleted() >= badge.getThreshold();
            case COURSE_COMPLETION:
                return user.getCoursesCompleted() >= badge.getThreshold();
           /* case TIME_BASED:
                return checkUserMembershipDuration(user, badge);
            case LEADERBOARD:
                return checkUserOnLeaderboard(user, badge);
            case EVENT_PARTICIPATION:
                return user.getEventsParticipated() >= badge.getThreshold();
            case CUSTOM_GOAL:
                return checkCustomGoalCompletion(user, badge);*/
            default:
                return false;
        }
    }

// Additional helper methods for specific badge rules

    // Check if the user has been a member for a specific time duration
    /* private boolean checkUserMembershipDuration(YtmsUserDto user, BadgeDto badge) {
        long membershipDuration = getUserMembershipDurationInDays(user);
        return membershipDuration >= badge.getThreshold();
    }

   // Check if the user is on a leaderboard for a particular achievement
    private boolean checkUserOnLeaderboard(YtmsUserDto user, BadgeDto badge) {
        // Logic to check if the user is on the leaderboard for a specific badge
        return user.isOnLeaderboard();
    }

    // Custom goal-based logic (e.g., specific achievements the user must meet)
    private boolean checkCustomGoalCompletion(YtmsUserDto user, BadgeDto badge) {
        // Implement custom logic for goal completion
        return user.getCustomGoalsCompleted() >= badge.getThreshold();
    }

    // Helper method to calculate user's membership duration in days
    private long getUserMembershipDurationInDays(YtmsUserDto user) {
        // Assuming user.getJoinDate() returns the date when the user joined
        return java.time.Duration.between(user.getJoinDate(), java.time.LocalDate.now()).toDays();
    }*/

    // Utility method to convert Badge entity to DTO
    private BadgeDto convertToDto(Badge badge) {
        // Implement logic to map the Badge entity to BadgeDto
        return new BadgeDto();  // Placeholder - replace with actual mapping logic
    }

}