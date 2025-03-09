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

class IBadgeServiceImplTest {

    @Mock
    private BadgeRepository badgeRepository;

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
}
