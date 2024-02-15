package com.yash.ytms.security.jwt;

import com.yash.ytms.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private final String secret = "secret";

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims :: getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims :: getExpiration);
    }

    public String getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("roles");
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    protected Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        GrantedAuthority authority = customUserDetails.getGrantedAuthorities();
        claims.put("roles", authority.getAuthority());
        claims.put("fullName", customUserDetails.getFullName());
        return doGenerateToken(claims, customUserDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 100))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String userName = getUserNameFromToken(token);
        final String roles = getRolesFromToken(token);
        return (StringUtils.equals(userName, userDetails.getUsername()))
                && !isTokenExpired(token)
                && StringUtils.equals(userDetails.getGrantedAuthorities().getAuthority(), roles);
    }
}
