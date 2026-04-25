package com.andhug.relay.gateway.infrastructure.persistence;

import com.andhug.relay.realtime.domain.repository.PresenceRepository;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.ProfilePresence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresenceRepositoryImpl implements PresenceRepository {

  private static final String BASE_CACHE_KEY = "presence:profile:";

  private final RedisTemplate<String, ProfilePresence> profilePresenceRedisTemplate;

  @Override
  public void saveProfilePresence(ProfileId profileId, ProfilePresence profilePresence) {
    profilePresenceRedisTemplate.opsForValue().set(getCacheKey(profileId), profilePresence);
  }

  @Override
  public ProfilePresence findByProfileId(ProfileId profileId) {
    return profilePresenceRedisTemplate.opsForValue().get(getCacheKey(profileId));
  }

  private String getCacheKey(ProfileId profileId) {
    return BASE_CACHE_KEY + profileId.toString();
  }
}
