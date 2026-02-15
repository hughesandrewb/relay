package com.andhug.relay.profile;

import com.andhug.relay.profile.exception.ProfileNotFoundException;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileMapper;
import com.andhug.relay.profile.internal.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    @Transactional
    public Profile getOrCreateProfile(Jwt jwt) {

        var sub = UUID.fromString(jwt.getSubject());

        log.info("Getting profile {}", sub);

        Profile profile;

        try {
            profile = getProfile(sub);
        } catch (ProfileNotFoundException e) {
            profile = createProfile(jwt);
        }

        return profile;
    }

    @Transactional(readOnly = true)
    public Profile getProfile(UUID profileId) {

        Optional<ProfileEntity> profileEntity = profileRepository.findById(profileId);

        return profileEntity
                .map(profileMapper::toDomain)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));
    }

    @Transactional
    private Profile createProfile(Jwt jwt) {

        var profileEntity = new ProfileEntity();

        profileEntity.setId(UUID.fromString(jwt.getSubject()));
        profileEntity.setUsername(jwt.getClaimAsString("preferred_username"));
        profileEntity.setDisplayName(jwt.getClaimAsString("name"));

        return profileMapper.toDomain(profileRepository.save(profileEntity));
    }
}
