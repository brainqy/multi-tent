package com.yash.ytms.services.IServices;

import com.yash.ytms.domain.YtmsUser;
import com.yash.ytms.domain.resume.Secret;
import com.yash.ytms.repository.SecretRepository;
import com.yash.ytms.repository.YtmsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 09-11-2024
 */
@Service
public class SecretService {
    private final SecretRepository secretRepository;
    @Autowired
    private YtmsUserRepository userRepository;
    YtmsUser user;
    @Autowired
    public SecretService(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }
    public void saveSecret(String key, String value, Principal principal) {
        String ownerEmail = principal.getName();
        Optional<YtmsUser> optionalUser = userRepository.getUserByEmail(ownerEmail);
        if(optionalUser.isPresent()){
             user = optionalUser.get();
        }
        // Look for an existing secret with this API key
        Optional<Secret> existingSecret = Optional.ofNullable(secretRepository.findBySecretKey(key,ownerEmail));

        if (existingSecret.isPresent()) {
            // Update the existing secret's value
            Secret secret = existingSecret.get();
            secret.setSecretValue(value);
            secret.setSecretKey(key);
            secret.setOwner(user);
            secretRepository.save(secret);
        } else {
            // Create a new secret entry if none exists
            Secret newSecret = new Secret(key, value);
            newSecret.setOwner(user);
            secretRepository.save(newSecret);
        }
    }


    public Optional<String> getSecretValue(String key,String ownerEmail) {
        Secret secret = secretRepository.findBySecretKey(key,ownerEmail);
        return secret != null ? Optional.of(secret.getSecretValue()) : Optional.empty();
    }
}
