package com.andhug.relay.auth;

import com.andhug.relay.profile.application.command.CreateProfileCommand;
import com.andhug.relay.profile.application.service.ProfileCommandService;
import com.andhug.relay.profile.application.service.ProfileService;
import com.andhug.relay.profile.domain.exception.ProfileNotFoundException;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.shared.domain.model.ProfileId;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProfileFilter extends OncePerRequestFilter {

    private final ProfileCommandService profileCommandService;

    private final ProfileService profileService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken
            && !(authentication.getPrincipal() instanceof Profile)) {
            Jwt jwt = jwtAuthenticationToken.getToken();

            Profile profile = getOrCreateProfile(jwt);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(profile, jwt, authentication.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private Profile getOrCreateProfile(Jwt jwt) {
        Profile profile;

        var profileId = ProfileId.of(UUID.fromString(jwt.getSubject()));

        try {
            profile = profileService.getProfile(profileId);
        } catch (ProfileNotFoundException e) {
            var createProfileCommand = CreateProfileCommand.builder()
                .userId(profileId)
                .username(jwt.getClaim("preferred_username"))
                .displayName(Optional.empty())
                .accentColor(Optional.empty())
                .build();

            profile = profileCommandService.createProfile(createProfileCommand);
        }

        return profile;
    }
}
