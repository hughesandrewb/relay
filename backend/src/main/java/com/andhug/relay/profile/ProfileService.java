package com.andhug.relay.profile;

import com.andhug.relay.profile.exception.ProfileNotFoundException;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileMapper;
import com.andhug.relay.profile.internal.ProfileRepository;
import com.andhug.relay.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    private final RedisTemplate<String, Profile> profileRedisTemplate;

    @Transactional
    public Profile getOrCreateProfile(Jwt jwt) {

        var sub = UUID.fromString(jwt.getSubject());

        log.info("Getting profile {}", sub);

        Profile profile;

        try {
            profile = getProfile(sub);
        } catch (ProfileNotFoundException e) {
            log.info("Creating new profile {}", sub);
            profile = createProfile(jwt);
        }

        return profile;
    }

    @Transactional(readOnly = true)
    public Profile getProfile(UUID profileId) {

        String cacheKey = String.format("profile:%s", profileId);

        Profile cachedProfile = profileRedisTemplate.opsForValue().get(cacheKey);

        if (cachedProfile != null) {
            log.info("Cache hit for profile {}", profileId);
            return cachedProfile;
        }
        log.info("Cache miss for profile {}", profileId);

        Optional<ProfileEntity> profileEntity = profileRepository.findById(profileId);

        Profile profile = profileEntity
                .map(profileMapper::toDomain)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        profileRedisTemplate.opsForValue().set(cacheKey, profile);

        return profile;
    }

    @Transactional(readOnly = true)
    public Map<UUID, Profile> getProfiles(List<UUID> profileIds) {

        List<String> cacheKeys = profileIds.stream()
                .distinct()
                .map(profileId -> String.format("profile:%s", profileId))
                .toList();
        List<Profile> cachedProfiles = profileRedisTemplate.opsForValue().multiGet(cacheKeys);

        Map<UUID, Profile> result = new HashMap<>();
        List<UUID> missingProfileIds = new ArrayList<>();

        for (int i = 0; i < cachedProfiles.size(); i++) {
            if (cachedProfiles.get(i) != null) {
                log.info("Cache hit for profile {}", profileIds.get(i));
                result.put(cachedProfiles.get(i).getId(), cachedProfiles.get(i));
                continue;
            }
            log.info("Cache miss for profile {}", profileIds.get(i));
            missingProfileIds.add(profileIds.get(i));
        }

        if (!missingProfileIds.isEmpty()) {
            List<Profile> profiles = profileRepository.findAllById(missingProfileIds).stream()
                    .map(profileMapper::toDomain)
                    .toList();

            Map<String, Profile> cacheMap = profiles.stream()
                    .collect(Collectors.toMap(profile -> String.format("profile:%s", profile.getId()), profile -> profile));
            profileRedisTemplate.opsForValue().multiSet(cacheMap);

            profiles.forEach(profile -> result.put(profile.getId(), profile));
        }

        return result;
    }

    @Transactional
    private Profile createProfile(Jwt jwt) {

        var profile = Profile.builder()
                .id(UUID.fromString(jwt.getSubject()))
                .username(jwt.getClaimAsString("preferred_username"))
                .displayName(jwt.getClaimAsString("name"))
                .accentColor(RandomUtils.generateRandomColor())
                .build();

        ProfileEntity profileEntity = profileMapper.toEntity(profile);

        return profileMapper.toDomain(profileRepository.save(profileEntity));
    }
}
