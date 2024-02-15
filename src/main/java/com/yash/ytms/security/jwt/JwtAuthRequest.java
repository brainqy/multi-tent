package com.yash.ytms.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthRequest {

    private String email;

    private String password;
}
