package com.yash.ytms.controller;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-11-2024
 */
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.services.IServices.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/secrets")
public class SecretController {

    private final SecretService secretService;

    @Autowired
    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping("/add")
    public ResponseWrapperDto addSecret(@RequestBody Map<String, String> payload, Principal principal) {
        String secret = payload.get("secret");
        String key = payload.get("key");
        secretService.saveSecret(key, secret,principal);
        ResponseWrapperDto dto= new ResponseWrapperDto();
        dto.setStatus("SUCCESS");
        dto.setMessage("secret saved");
        return dto;
    }

    @GetMapping("/{key}")
    public String getSecret(@PathVariable String key,Principal principal) {
        String ownerEmail=principal.getName();
        Optional<String> secretValue = secretService.getSecretValue(key,ownerEmail);
        return secretValue.orElse("Secret not found!");
    }
}

