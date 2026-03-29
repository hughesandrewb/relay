package com.andhug.relay.room.directmessage.domain.repository;

import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.List;

public interface DirectMessageRepository {
  List<DirectMessage> findByProfileId(ProfileId profileId);

  void save(DirectMessage directMessage);
}
