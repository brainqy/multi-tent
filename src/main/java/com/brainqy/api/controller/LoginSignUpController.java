package com.brainqy.api.controller;

import com.brainqy.api.dto.YtmsUserDto;
import com.brainqy.api.security.jwt.JwtAuthRequest;
import com.brainqy.api.security.jwt.JwtAuthResponse;
import com.brainqy.api.services.IServices.IAuthService;
import com.brainqy.api.services.IServices.IYtmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
@RestController
public class LoginSignUpController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private IYtmsUserService userService;

    @Autowired
    private IYtmsUserService ytmsUserService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest authRequest) {
        JwtAuthResponse authResponse = this
                .authService
                .login(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<YtmsUserDto> register(@RequestBody YtmsUserDto userDto) {
        YtmsUserDto newUser = this
                .userService
                .createNewUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @GetMapping("/get/user")
    public ResponseEntity<YtmsUserDto> getUserByEmail(@RequestParam String email) {
        YtmsUserDto user = this
                .ytmsUserService
                .getUserByEmailAdd(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
