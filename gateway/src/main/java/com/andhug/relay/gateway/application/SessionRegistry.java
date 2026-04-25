package com.andhug.relay.gateway.application;

import com.andhug.relay.gateway.domain.model.Session;
import com.andhug.relay.gateway.domain.model.SessionId;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SessionRegistry {

  private final Map<String, Session> sessionIdToSession = new ConcurrentHashMap<>();

  private final Map<UUID, Set<String>> profileIdToSessionId = new ConcurrentHashMap<>();

  public Session createSession(SessionId sessionId, ProfileId profileId) {
    Session session = new Session(sessionId.value());
    sessionIdToSession.put(sessionId.value(), session);
    addSessionToProfile(profileId, sessionId);
    log.info("Created session {} for profile {}", sessionId.toString(), profileId.toString());
    return session;
  }

  public void deleteSession(SessionId sessionId, ProfileId profileId) {
    sessionIdToSession.remove(sessionId.value());
    removeSessionFromProfile(profileId, sessionId);
    log.info("Deleted session {} for profile {}", sessionId.toString(), profileId.toString());
  }

  public Session getSession(SessionId sessionId) {
    return sessionIdToSession.get(sessionId.value());
  }

  public Set<Session> getSessionsByProfileId(ProfileId profileId) {
    Set<String> sessionIds = profileIdToSessionId.get(profileId.value());
    if (sessionIds == null) {
      return Set.of();
    }
    return sessionIds.stream()
        .map(sessionIdToSession::get)
        .filter(session -> session != null)
        .collect(Collectors.toSet());
  }

  private void addSessionToProfile(ProfileId profileId, SessionId sessionId) {
    profileIdToSessionId
        .computeIfAbsent(profileId.value(), k -> ConcurrentHashMap.newKeySet())
        .add(sessionId.value());
  }

  private void removeSessionFromProfile(ProfileId profileId, SessionId sessionId) {
    Set<String> sessionIds = profileIdToSessionId.get(profileId.value());

    if (sessionIds == null) {
      log.warn("Profile {} has no sessions", profileId.toString());
      return;
    }

    sessionIds.remove(sessionId.value());

    if (sessionIds.size() == 0) {
      profileIdToSessionId.remove(profileId.value());
      log.info("Last session for profile {} removed, removed profile");
    }
  }
}
