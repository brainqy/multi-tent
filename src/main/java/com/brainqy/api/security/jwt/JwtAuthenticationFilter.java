package com.brainqy.api.security.jwt;

import com.brainqy.api.security.userdetails.CustomUserDetails;
import com.brainqy.api.security.userdetails.CustomUserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        if (StringUtils.isNotEmpty(header)) {
            if (StringUtils.startsWith(header, TOKEN_PREFIX)) {
                token = StringUtils.substring(header, 7);
                try {
                    userName = this.jwtTokenHelper.getUserNameFromToken(token);
                } catch (IllegalArgumentException e) {
                    LOGGER.error("Unable to get Jwt Token !!", e);
                    setErrorResponse(HttpServletResponse.SC_BAD_REQUEST, response, "Unable to get Jwt Token !!");
                    return;
                } catch (ExpiredJwtException e) {
                    LOGGER.error("Jwt Token has expired !!", e);
                    setErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, "Jwt Token has expired !!");
                    return;
                } catch (MalformedJwtException e) {
                    LOGGER.error("Invalid Jwt !!", e);
                    setErrorResponse(HttpServletResponse.SC_BAD_REQUEST, response, "Invalid Jwt Token !!");
                    return;
                }
            } else {
                LOGGER.info("Jwt Token does not begin with Bearer.");
            }
        }

        if (StringUtils.isNotEmpty(userName)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            if (ObjectUtils.isNotEmpty(userDetails)) {
                if (this.jwtTokenHelper.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    LOGGER.error("Invalid Jwt token !!");
                    setErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, "Invalid Jwt token !!");
                    return;
                }
            } else {
                LOGGER.error("Wrong User details provided !!");
                setErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, response, "Wrong User details provided !!");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(int status, HttpServletResponse response, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
