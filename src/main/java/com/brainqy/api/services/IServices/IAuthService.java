package com.brainqy.api.services.IServices;

import com.brainqy.api.security.jwt.JwtAuthRequest;
import com.brainqy.api.security.jwt.JwtAuthResponse;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
public interface IAuthService {

    JwtAuthResponse login(JwtAuthRequest authRequest);

}
