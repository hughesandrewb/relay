package com.andhug.relay.persence.domain.repository;

import com.andhug.relay.persence.domain.model.MemberPresence;

public interface PresenceRepository {
  public void save(MemberPresence presenceRecord);
}
