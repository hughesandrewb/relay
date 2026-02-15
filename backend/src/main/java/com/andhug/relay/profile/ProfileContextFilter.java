package com.andhug.relay.profile;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class ProfileContextFilter extends OncePerRequestFilter {

    ProfileService profileService;

    public ProfileContextFilter(ProfileService profileService) {

        this.profileService = profileService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Jwt jwt) {

            Profile profile = profileService.getOrCreateProfile(jwt);

            ProfileContext.setCurrentProfile(profile);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            ProfileContext.clear();
        }
    }
}
