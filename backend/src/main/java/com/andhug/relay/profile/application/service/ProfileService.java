package com.andhug.relay.profile.application.service;

import com.andhug.relay.profile.application.mapper.ProfileMapper;
import com.andhug.relay.profile.domain.exception.ProfileNotFoundException;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.infrastructure.persistence.ProfileEntity;
import com.andhug.relay.profile.infrastructure.persistence.ProfileJpaRepository;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileJpaRepository profileRepository;

  private final ProfileMapper profileMapper;

  private final RedisTemplate<String, Profile> profileRedisTemplate;

  @Transactional(readOnly = true)
  public Profile getProfile(ProfileId profileId) {

    // String cacheKey = String.format("profile:%s", profileId);

    // Profile cachedProfile = profileRedisTemplate.opsForValue().get(cacheKey);

    // if (cachedProfile != null) {
    //     log.info("Cache hit for profile {}", profileId);
    //     return cachedProfile;
    // }
    log.info("Cache miss for profile {}", profileId);

    Optional<ProfileEntity> profileEntity = profileRepository.findById(profileId.value());

    Profile profile =
        profileEntity
            .map(profileMapper::toDomain)
            .orElseThrow(() -> new ProfileNotFoundException(profileId.value()));

    // profileRedisTemplate.opsForValue().set(cacheKey, profile);

    return profile;
  }

  @Transactional(readOnly = true)
  public Map<UUID, Profile> getProfiles(List<UUID> profileIds) {

    // List<String> cacheKeys = profileIds.stream()
    //         .distinct()
    //         .map(profileId -> String.format("profile:%s", profileId))
    //         .toList();
    // List<Profile> cachedProfiles = profileRedisTemplate.opsForValue().multiGet(cacheKeys);

    Map<UUID, Profile> result = new HashMap<>();
    List<UUID> missingProfileIds = new ArrayList<>();

    // for (int i = 0; i < cachedProfiles.size(); i++) {
    //     if (cachedProfiles.get(i) != null) {
    //         log.info("Cache hit for profile {}", profileIds.get(i));
    //         result.put(cachedProfiles.get(i).getId(), cachedProfiles.get(i));
    //         continue;
    //     }
    //     log.info("Cache miss for profile {}", profileIds.get(i));
    //     missingProfileIds.add(profileIds.get(i));
    // }

    if (!missingProfileIds.isEmpty()) {
      List<Profile> profiles =
          profileRepository.findAllById(missingProfileIds).stream()
              .map(profileMapper::toDomain)
              .toList();

      Map<String, Profile> cacheMap =
          profiles.stream()
              .collect(
                  Collectors.toMap(
                      profile -> String.format("profile:%s", profile.getId()), profile -> profile));
      profileRedisTemplate.opsForValue().multiSet(cacheMap);

      profiles.forEach(profile -> result.put(profile.getId().value(), profile));
    }

    return result;
  }
}
