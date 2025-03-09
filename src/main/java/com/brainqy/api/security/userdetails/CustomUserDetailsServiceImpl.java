package com.brainqy.api.security.userdetails;

import com.brainqy.api.domain.YtmsUser;
import com.brainqy.api.exception.ApplicationException;
import com.brainqy.api.repository.YtmsUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
@Service
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private YtmsUserRepository userRepository;


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<YtmsUser> user = this
                .userRepository
                .getUserByEmail(username);

        if (ObjectUtils.isNotEmpty(user)) {
            return new CustomUserDetails(user.get());
        } else
            throw new ApplicationException("User Not Found with this username/email");
    }
}
