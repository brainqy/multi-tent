package com.yash.ytms.services.IServices;

import com.yash.ytms.security.jwt.JwtAuthRequest;
import com.yash.ytms.security.jwt.JwtAuthResponse;

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
