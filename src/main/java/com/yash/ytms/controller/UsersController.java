package com.yash.ytms.controller;

import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.services.IServices.IYtmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IYtmsUserService userService;

    @PreAuthorize("hasRole('ROLE_TECHNICAL_MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<List<YtmsUserDto>> getPendingUsers() {
        return new ResponseEntity<>(userService.getAllPendingUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_TECHNICAL_MANAGER')")
    @PostMapping("/approve")
    public ResponseEntity<Boolean> approvePendingUser(@RequestParam String emailAdd) {
        Boolean status = this.userService.approvePendingUser(emailAdd);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_TECHNICAL_MANAGER')")
    @PostMapping("/decline")
    public ResponseEntity<Boolean> declinePendingUser(@RequestParam String emailAdd) {
        Boolean status = this.userService.declinePendingUser(emailAdd);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/forgotPassword")
    public ResponseEntity<ResponseWrapperDto> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public Boolean resetPassword(@RequestBody Map<String, String> map) {
        return this.userService.resetPassword(map);
    }

    @PostMapping("/changePassword")
    public ResponseWrapperDto changePassword(@RequestBody Map<String, String> map) {
        return this.userService.changePassword(map);
    }

    @GetMapping("/get/all-trainers")
    public ResponseEntity<List<YtmsUserDto>> getAllTrainers() {
        List<YtmsUserDto> allTrainers = this.userService.getAllTrainers();
        return new ResponseEntity<>(allTrainers, HttpStatus.OK);
    }
}
