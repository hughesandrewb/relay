package com.andhug.relay.profile.infrastructure.persistence;

import com.andhug.relay.profile.application.mapper.ProfileMapper;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

  private final ProfileJpaRepository profileJpaRepository;

  private final RedisTemplate<String, Profile> profileRedisTemplate;

  private final ProfileMapper profileMapper;

  @Override
  public Profile findById(ProfileId profileId) {

    Profile cachedProfile = profileRedisTemplate.opsForValue().get(profileId.getCacheKey());

    if (cachedProfile != null) {
      log.info("Cache hit for profile {}", profileId);
      return cachedProfile;
    }
    log.info("Cache miss for profile {}", profileId);

    Profile profile =
        profileJpaRepository
            .findById(profileId.value())
            .map(profileMapper::toDomain)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format("Profile could not be found, id: %s", profileId.toString())));

    profileRedisTemplate.opsForValue().set(profileId.getCacheKey(), profile);

    return profile;
  }

  @Override
  public List<Profile> findAllById(List<ProfileId> profileIds) {

    List<String> cacheKeys =
        profileIds.stream().map(ProfileId::getCacheKey).collect(Collectors.toList());

    List<Profile> cacheResults = profileRedisTemplate.opsForValue().multiGet(cacheKeys);

    List<Profile> cachedProfiles = new ArrayList<>();
    List<ProfileId> missedProfileIds = new ArrayList<>();

    for (int i = 0; i < cacheKeys.size(); i++) {
      Profile cachedProfile = cacheResults.get(i);
      if (cachedProfile != null) {
        log.info("Cache hit for profile {}", profileIds.get(i));
        cachedProfiles.add(cachedProfile);
      } else {
        log.info("Cache miss for profile {}", profileIds.get(i));
        missedProfileIds.add(profileIds.get(i));
      }
    }

    if (missedProfileIds.isEmpty()) {
      return cachedProfiles;
    }

    List<UUID> uuids = missedProfileIds.stream().map(ProfileId::value).toList();
    List<Profile> dbProfiles =
        profileJpaRepository.findAllById(uuids).stream().map(profileMapper::toDomain).toList();

    Map<String, Profile> cacheMap =
        dbProfiles.stream()
            .collect(
                Collectors.toMap(profile -> profile.getId().getCacheKey(), profile -> profile));
    profileRedisTemplate.opsForValue().multiSet(cacheMap);

    Map<ProfileId, Profile> profileMap = new HashMap<>();
    for (Profile profile : Stream.concat(cachedProfiles.stream(), dbProfiles.stream()).toList()) {
      profileMap.put(profile.getId(), profile);
    }

    return profileIds.stream().map(profileMap::get).toList();
  }

  @Override
  public List<Profile> getProfiles(List<ProfileId> profileIds) {
    Set<UUID> uuids = profileIds.stream().map(ProfileId::value).collect(Collectors.toSet());

    return profileJpaRepository.findAllById(uuids).stream().map(profileMapper::toDomain).toList();
  }

  @Override
  public Profile save(Profile profile) {
    ProfileEntity profileEntity = profileMapper.toEntity(profile);

    profileJpaRepository.save(profileEntity);

    return profileMapper.toDomain(profileEntity);
  }
}
