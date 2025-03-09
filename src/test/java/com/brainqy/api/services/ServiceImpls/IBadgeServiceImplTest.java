package com.brainqy.api.services.ServiceImpls;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-03-2025
 */

import com.brainqy.api.constants.BadgeRule;
import com.brainqy.api.domain.Badge;
import com.brainqy.api.dto.BadgeDto;
import com.brainqy.api.repository.BadgeRepository;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.brainqy.api.constants.BadgeRule;
import com.brainqy.api.domain.Badge;
import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.dto.BadgeDto;
import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.repository.BadgeRepository;
import com.brainqy.api.repository.YtmsUserRepository;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IBadgeServiceImplTest {

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private YtmsUserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private IYtmsUserService userService;

    @InjectMocks
    private IBadgeServiceImpl badgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBadge_ShouldFailWithEmptyBadgeName() {
        BadgeDto badgeDto = new BadgeDto("", "Description", "icon.png", "#FFFFFF", BadgeRule.XP_BASED, 100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            badgeService.createBadge(badgeDto);
        });

        assertEquals("Badge name cannot be empty", exception.getMessage());
    }

    @Test
    void createBadge_ShouldFailWithNullBadgeDescription() {
        BadgeDto badgeDto = new BadgeDto("Badge Name", null, "icon.png", "#FFFFFF", BadgeRule.XP_BASED, 100);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            badgeService.createBadge(badgeDto);
        });

        assertEquals("Badge description cannot be null", exception.getMessage());
    }

    @Test
    void createBadge_ShouldFailWithInvalidBadgeThreshold() {
        BadgeDto badgeDto = new BadgeDto("Badge Name", "Description", "icon.png", "#FFFFFF", BadgeRule.XP_BASED, -1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            badgeService.createBadge(badgeDto);
        });

        assertEquals("Badge threshold must be non-negative", exception.getMessage());
    }

    @Test
    void findBadgeById_ShouldReturnNullForNonExistentBadgeId() {
        when(badgeRepository.findById(anyLong())).thenReturn(Optional.empty());

        BadgeDto badgeDto = badgeService.findBadgeById(999L);

        assertNull(badgeDto);
    }

    @Test
    void findBadgeByName_ShouldReturnNullForNonExistentBadgeName() {
        when(badgeRepository.findByNameIgnoreCase(anyString())).thenReturn(null);

        BadgeDto badgeDto = badgeService.findBadgeByName("NonExistentBadge");

        assertNull(badgeDto);
    }

    @Test
    void getAllBadges_ShouldReturnAllBadges() {
        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge(1L, "Badge1", "Description1", "icon1.png", "#FFFFFF", BadgeRule.XP_BASED, 100, null));
        badges.add(new Badge(2L, "Badge2", "Description2", "icon2.png", "#000000", BadgeRule.TASK_COMPLETION, 200, null));

        when(badgeRepository.findAll()).thenReturn(badges);
        when(mapper.map(any(Badge.class), eq(BadgeDto.class))).thenReturn(new BadgeDto());

        List<BadgeDto> result = badgeService.getAllBadges();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void checkBadgeEligibility_ShouldReturnTrueForEligibleUser() {
        YtmsUserDto userDto = new YtmsUserDto();
        userDto.setXpPoints(150);
        BadgeDto badgeDto = new BadgeDto("Badge1", "Description1", "icon1.png", "#FFFFFF", BadgeRule.XP_BASED, 100);

        boolean result = badgeService.checkBadgeEligibility(userDto, badgeDto);

        assertTrue(result);
    }

    @Test
    void checkBadgeEligibility_ShouldReturnFalseForIneligibleUser() {
        YtmsUserDto userDto = new YtmsUserDto();
        userDto.setXpPoints(50);
        BadgeDto badgeDto = new BadgeDto("Badge1", "Description1", "icon1.png", "#FFFFFF", BadgeRule.XP_BASED, 100);

        boolean result = badgeService.checkBadgeEligibility(userDto, badgeDto);

        assertFalse(result);
    }

@Test
void assignBadgesToUser_ShouldAssignEligibleBadges() {
    YtmsUserDto userDto = new YtmsUserDto();
    userDto.setXpPoints(150);
    List<Badge> badges = new ArrayList<>();
    Badge badge = new Badge(1L, "Badge1", "Description1", "icon1.png", "#FFFFFF", BadgeRule.XP_BASED, 100, null);
    badges.add(badge);

    when(badgeRepository.findAll()).thenReturn(badges);
    when(mapper.map(any(Badge.class), eq(BadgeDto.class))).thenAnswer(invocation -> {
        Badge source = invocation.getArgument(0);
        return new BadgeDto(source.getName(), source.getDescription(), source.getIcon(), source.getBackgroundColor(), source.getRule(), source.getThreshold());
    });
    when(mapper.map(any(YtmsUserDto.class), eq(YtmsUser.class))).thenAnswer(invocation -> {
        YtmsUser user = new YtmsUser();
        user.setEarnedBadges(new ArrayList<>()); // Initialize the earnedBadges list
        return user;
    });

    List<BadgeDto> result = badgeService.assignBadgesToUser(userDto);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(userRepository, times(1)).save(any(YtmsUser.class));
}

    @Test
    void assignEligibleBadgesToAllUsers_ShouldAssignBadgesToAllUsers() {
        List<YtmsUserDto> users = new ArrayList<>();
        users.add(new YtmsUserDto());

        when(userService.getAllUsers()).thenReturn(users);

        badgeService.assignEligibleBadgesToAllUsers();

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAssignedBadgesByUserName_ShouldReturnAssignedBadges() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testuser");

        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge(1L, "Badge1", "Description1", "icon1.png", "#FFFFFF", BadgeRule.XP_BASED, 100, null));

        when(badgeRepository.getAssignedBadgesByUserName(anyString())).thenReturn(badges);
        when(mapper.map(any(Badge.class), eq(BadgeDto.class))).thenReturn(new BadgeDto());

        List<BadgeDto> result = badgeService.getAssignedBadgesByUserName(principal);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
