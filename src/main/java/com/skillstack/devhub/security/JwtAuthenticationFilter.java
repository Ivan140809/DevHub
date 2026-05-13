package com.skillstack.devhub.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;


    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            String email = jwtUtil.extractEmail(token);
            logger.debug("EMAIL JWT: {}", email);
            if (email != null) {
                var currentAuth = SecurityContextHolder.getContext().getAuthentication();
                boolean shouldAuthenticate = currentAuth == null
                        || !currentAuth.isAuthenticated()
                        || "anonymousUser".equals(currentAuth.getName());

                if (shouldAuthenticate) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    logger.debug("USERDETAILS: {}", userDetails.getUsername());
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    logger.debug("AUTH NAME: {}", SecurityContextHolder.getContext().getAuthentication().getName());
                    logger.debug("AUTHORITIES: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                }
            }
        } catch (Exception e) {
            logger.error("ERROR JWT FILTER: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}