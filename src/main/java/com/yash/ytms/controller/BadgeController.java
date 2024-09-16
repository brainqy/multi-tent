package com.yash.ytms.controller;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 14-09-2024
 */
import com.yash.ytms.domain.Badge;
import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.dto.BadgeDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.services.IServices.BadgeService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    @Autowired
    private BadgeService badgeService;
    @Autowired
    private IYtmsUserService userService;

    // Create a new badge
    @PostMapping
    public ResponseEntity<Void> createBadge(@RequestBody BadgeDto badgeDto) {
        badgeService.createBadge(badgeDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Get all badges
    @GetMapping
    public ResponseEntity<List<BadgeDto>> getAllBadges() {
        List<BadgeDto> badges = badgeService.getAllBadges();
        return new ResponseEntity<>(badges, HttpStatus.OK);
    }

    // Get a badge by ID
    @GetMapping("/{id}")
    public ResponseEntity<BadgeDto> getBadgeById(@PathVariable Long id) {
        BadgeDto badgeDto = badgeService.findBadgeById(id);
        if (badgeDto != null) {
            return new ResponseEntity<>(badgeDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/assignEligible")
    public ResponseEntity<List<BadgeDto>> assignBadges(Principal principal) {
        String userName= principal.getName();
        YtmsUserDto userOptional = userService.getUserByEmailAdd(userName);
        if (userOptional!=null) {

            List<BadgeDto> badges = badgeService.assignBadgesToUser(userOptional);
            return ResponseEntity.ok(badges);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get a badge by name
    @GetMapping("/name/{name}")
    public ResponseEntity<BadgeDto> getBadgeByName(@PathVariable String name) {
        BadgeDto badgeDto = badgeService.findBadgeByName(name);
        if (badgeDto != null) {
            return new ResponseEntity<>(badgeDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getMyBadges")
    public ResponseEntity<List<BadgeDto>> getAssignedBadgesByUserName(Principal principal) {
        List<BadgeDto> badges = badgeService.getAssignedBadgesByUserName(principal);
        return ResponseEntity.ok(badges);
    }
}

