package com.yash.ytms.services.ServiceImpls;

import com.yash.ytms.domain.Badge;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.BadgeDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.repository.BadgeRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import com.yash.ytms.services.IServices.BadgeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BadgeServiceImpl implements BadgeService {
    @Autowired
    private BadgeRepository badgeRepository;
    @Autowired
    private YtmsUserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void createBadge(BadgeDto badgeDto) {
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

}